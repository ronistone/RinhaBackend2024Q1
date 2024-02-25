package controller

import (
	model2 "RinhabackendGo/controller/model"
	"RinhabackendGo/model"
	"RinhabackendGo/repository"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
)

type ClientController struct {
	ClientRepository repository.ClientRepository
}

func (c ClientController) Register(echo *echo.Echo) {
	client := echo.Group("/clientes")
	client.POST("/:id/transacoes", c.MakeTransaction)
	client.GET("/:id/extrato", c.GetExtract)
}

func (c ClientController) MakeTransaction(ctx echo.Context) error {
	var transaction model.Transaction
	if err := ctx.Bind(&transaction); err != nil {
		return ctx.NoContent(http.StatusBadRequest)
	}
	clientId := ctx.Param("id")
	clientIdValue, err := strconv.Atoi(clientId)
	if err != nil || (transaction.Type != "d" && transaction.Type != "c") || len(transaction.Description) > 10 || len(transaction.Description) < 1 {
		return ctx.NoContent(http.StatusUnprocessableEntity)
	}
	transaction.ClientId = clientIdValue
	if clientIdValue < 1 || clientIdValue > 5 {
		return ctx.NoContent(http.StatusNotFound)
	}
	client, err := c.ClientRepository.MakeTransaction(ctx.Request().Context(), transaction)

	if err != nil {
		//ctx.Logger().Errorf("Error making transaction %v", err)
		return ctx.NoContent(http.StatusUnprocessableEntity)
	}

	return ctx.JSON(http.StatusOK, model2.TransactionCreate{Balance: client.Value, Limit: client.Limit})

}

func (c ClientController) GetExtract(ctx echo.Context) error {
	clientId := ctx.Param("id")
	clientIdValue, err := strconv.Atoi(clientId)
	if err != nil {
		return ctx.NoContent(http.StatusBadRequest)
	}
	if clientIdValue < 1 || clientIdValue > 5 {
		return ctx.NoContent(http.StatusNotFound)
	}

	client, err := c.ClientRepository.GetExtract(ctx.Request().Context(), clientIdValue)

	if err != nil {
		//ctx.Logger().Errorf("Error making transaction %v", err)
		return ctx.NoContent(http.StatusUnprocessableEntity)
	}

	return ctx.JSON(http.StatusOK, client)
}
