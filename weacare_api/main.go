package main

import (
	"weacare_api/routes"
	"weacare_api/db"
)


func main() {
	db.Init()
	e := routes.Init()
	e.Logger.Fatal(e.Start(":7070"))
}
