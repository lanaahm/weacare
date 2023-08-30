package models

import (
	"database/sql"
	"fmt"
	"net/http"
	"weacare_api/db"
	"weacare_api/helpers"
)

type User struct {
	Id       int    `json:"id"`
	Username string `json:"username"`
	Password string `json:"password"`
	Email    string `json:"email"`
}

func CheckLogin(username, password string) (bool, User, error) {
	var obj User
	var pwd string
	con := db.CreateCon()

	sqlStatement := "SELECT * FROM users WHERE username = ?"
	err := con.QueryRow(sqlStatement, username).Scan(
		&obj.Id, &obj.Username, &pwd, &obj.Email,
	)

	if err == sql.ErrNoRows {
		fmt.Print("Username not found!")
		return false, obj, err
	}

	if err != nil {
		fmt.Print("Query error!")
		return false, obj, err
	}

	match, err := helpers.CheckPasswordHash(password, pwd)
	if !match {
		fmt.Print("Hash and password doesn't match!")
		return false, obj, err
	}

	return true, obj, nil
}

func StoreUser(username string, password string, email string) (Response, error){
	var res Response
	con := db.CreateCon()

	sqlStatment := "INSERT INTO users (username, password, email) VALUES (?, ?, ?)" 
	stmt, err := con.Prepare(sqlStatment)
	
	if err != nil {
		return res, err
	}

	pwd, _ := helpers.HashPassword(password)

	result, err := stmt.Exec(username, pwd, email)

	if err != nil {
		return res, err
	}

	lastInsertId, err := result.LastInsertId()

	if err != nil {
		return res, err
	}
	
	res.Status = http.StatusOK 
	res.Message = "Success"
	res.Data = map[string] int64 {
		"last_insert_id": lastInsertId, 
	}

	return res, nil
}

//update data
func Updateuser(user_id int, username string, email string, password string)(Response, error){
	var res Response 
	con := db.CreateCon()	
	pwd, _ := helpers.HashPassword(password)
	sqlStatement := "UPDATE `users` SET password=?,email=?,username=? WHERE id=?" // tanda tanya itu masuk prepare statement
	stmt, err := con.Prepare(sqlStatement)
	if err != nil {
		return res, err
	}

	//meng execute lalu terima parameter dan usahakan harus urut dengan yang diinsert kan, jadi nim,name,gender,fakultas,prodi
	result, err := stmt.Exec(pwd, email, username, user_id)

	if err != nil {
		return res, err
	}
	//untuk mengecek sudah masuk apa tidak query nya
	rowAffected, err := result.RowsAffected()

	if err != nil {
		return res, err
	}

	res.Status = http.StatusOK 
	res.Message = "Success"
	res.Data = map[string] int64 {
		"data": rowAffected, 
	}
	return res, nil
}