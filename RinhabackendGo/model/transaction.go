package model

import "time"

type Transaction struct {
	Id          *int64     `json:"id" db:"ID"`
	ClientId    int        `json:"cliente_id" db:"CLIENTE_ID"`
	Value       int        `json:"valor" db:"valor"`
	Type        string     `json:"tipo" db:"tipo"`
	Description string     `json:"descricao" db:"descricao"`
	CreatedAt   *time.Time `json:"realizada_em" db:"realizada_em"`
}

type TransactionBalance struct {
	Value       int        `json:"valor" db:"valor"`
	Type        string     `json:"tipo" db:"tipo"`
	Description string     `json:"descricao" db:"descricao"`
	CreatedAt   *time.Time `json:"realizada_em" db:"realizada_em"`
}
