package repository

import (
	"RinhabackendGo/model"
	"context"
	"errors"
	"github.com/gocraft/dbr/v2"
)

type ClientRepository struct {
	DbConnection *dbr.Connection
}

func (c ClientRepository) MakeTransaction(ctx context.Context, transaction model.Transaction) (model.Client, error) {

	session := c.DbConnection.NewSession(nil)
	tx, _ := session.BeginTx(ctx, nil)
	defer tx.Commit()

	balanceStatement := tx.SelectBySql(`
	SELECT valor, limite FROM clientes WHERE id = ? LIMIT 1 FOR NO KEY UPDATE 
	`, transaction.ClientId)
	client := model.Client{}
	err := balanceStatement.LoadOne(&client)
	if err != nil {
		tx.Rollback()
		return model.Client{}, err
	}
	transactionValue := transaction.Value
	if transaction.Type == "d" {
		transactionValue = -transactionValue
	}
	client.Value = client.Value + transactionValue
	if client.Value < -client.Limit {
		tx.Rollback()
		return model.Client{}, errors.New("saldo insuficiente")
	}

	updateStatement := tx.UpdateBySql(`
 		UPDATE clientes SET valor = ? WHERE id = ?
 	`, client.Value, transaction.ClientId)

	_, err = updateStatement.Exec()
	if err != nil {
		tx.Rollback()
		return model.Client{}, err
	}
	transationStatement := tx.InsertBySql(`
		INSERT INTO transacoes(id, valor, cliente_id, tipo, descricao, realizada_em)
		    			values (default, ?, ?, ?, ?, now())
		`, transaction.Value, transaction.ClientId, transaction.Type, transaction.Description)

	_, err = transationStatement.Exec()
	if err != nil {
		tx.Rollback()
		return model.Client{}, err
	}
	return client, nil
}

//func (c ClientRepository) MakeTransactionSQL(ctx context.Context, transaction model.Transaction) (model.Client, error) {
//
//	session := c.DbConnection.NewSession(nil)
//	tx, _ := session.BeginTx(ctx, nil)
//	defer tx.Commit()
//	transactionValue := transaction.Value
//	if transaction.Type == "d" {
//		transactionValue = -transactionValue
//	}
//
//	balanceStatement := tx.SelectBySql(`
//		SELECT * FROM make_transaction(?, ?, ?, ?)
//	`, transaction.ClientId, transactionValue, transaction.Type, transaction.Description)
//	client := model.Client{}
//	err := balanceStatement.LoadOne(&client)
//	if err != nil {
//		tx.Rollback()
//		return model.Client{}, err
//	}
//	return client, nil
//}

func (c ClientRepository) GetExtract(ctx context.Context, id int) (model.Extract, error) {
	session := c.DbConnection.NewSession(nil)
	clientStatement := session.SelectBySql(`
	SELECT limite, valor, now() as data_extrato FROM clientes where id = ?
	`, id)

	var client model.ClientBalance
	err := clientStatement.LoadOne(&client)

	if err != nil {
		if errors.Is(err, dbr.ErrNotFound) {
			return model.Extract{}, errors.New("Cliente não encontrado")
		}
		return model.Extract{}, err
	}

	transactionsStatement := session.SelectBySql(`
	SELECT valor, tipo, descricao, realizada_em FROM transacoes where cliente_id = ? ORDER BY realizada_em DESC LIMIT 10
	`, id)

	var transactions []model.TransactionBalance
	_, err = transactionsStatement.Load(&transactions)
	if err != nil {
		return model.Extract{}, err
	}

	extract := model.Extract{
		Balance:     client,
		Transaction: transactions,
	}

	return extract, nil
}
