package main

import (
	controller2 "RinhabackendGo/controller"
	"RinhabackendGo/repository"
	"fmt"
	"github.com/gocraft/dbr/v2"
	"github.com/knadh/koanf/parsers/yaml"
	"github.com/knadh/koanf/providers/env"
	"github.com/knadh/koanf/providers/file"
	"github.com/knadh/koanf/v2"
	"github.com/labstack/echo/v4"
	"github.com/labstack/echo/v4/middleware"
	"github.com/labstack/gommon/log"
	_ "github.com/lib/pq"
	"strconv"
	"strings"
)

var k *koanf.Koanf

func Init() error {
	k = koanf.New(".")

	if err := k.Load(file.Provider("./config/config.yaml"), yaml.Parser()); err != nil {
		log.Fatalf("Fail to loading config: %v", err)
		return err
	}

	return k.Load(env.Provider("RINHA_", ".", func(s string) string {
		return strings.Replace(strings.ToLower(
			strings.TrimPrefix(s, "RINHA_")), "_", ".", -1)
	}), nil)

}

func GetDatabaseDSN() string {
	host := k.Get("database.host")
	username := k.Get("database.username")
	password := k.Get("database.password")
	port := k.Get("database.port")
	database := k.Get("database.name")

	return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable timezone=UTC",
		host, port, username, password, database)

}

func main() {
	Init()
	e := echo.New()
	e.Logger.SetLevel(log.OFF)
	e.Use(middleware.Recover())

	db, err := dbr.Open("postgres", GetDatabaseDSN(), nil)
	if err != nil {
		panic(err)
	}
	defer db.Close()
	maxConnections, err := strconv.Atoi(k.Get("database.maxconnections").(string))
	db.SetMaxOpenConns(maxConnections)

	repository := repository.ClientRepository{
		DbConnection: db,
	}

	controller := controller2.ClientController{
		ClientRepository: repository,
	}

	controller.Register(e)

	e.Start(":8080")

}
