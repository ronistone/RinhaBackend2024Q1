package model

import "time"

type Client struct {
	Id    int `json:"id" db:"id" `
	Limit int `json:"limite" db:"limite"`
	Value int `json:"saldo" db:"valor"`
}

type ClientBalance struct {
	Limit int       `json:"limite" db:"limite"`
	Value int       `json:"total" db:"valor"`
	Date  time.Time `json:"data_extrato" db:"data_extrato"`
}

type Extract struct {
	Balance     ClientBalance        `json:"saldo" db:"saldo"`
	Transaction []TransactionBalance `json:"ultimas_transacoes"`
}
