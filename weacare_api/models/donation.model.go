package models

import (
	"net/http"
	"weacare_api/db"

	"github.com/go-playground/validator"
)

type Donation struct{
	Donation_ID int `json:"donation_id"`
	Payment_method string `json:"payment_method"`
	User_donation int `json:"user_donation"`
}

type DonationSum struct{
	User_donation int `json:"user_donation"`
}


//Read All
func FetchAllDonation() (Response, error){
	var obj Donation
	var arrObj []Donation
	var res Response
//conn = connection
	con:= db.CreateCon()

	//untuk Get Mahasiswa
	sqlStatement := "SELECT * FROM `donation`" // ga pakai tanda petik sebelah angka 1 jg gpp

	rows, err := con.Query(sqlStatement)
	//perlu di close, biar ga banyak ke load dan biar ga lemot, karna kalau ga diputus dia kan kirim terus ga berhenti2 
	defer rows.Close()

	if err != nil {
		return res, err
	}
	//rows.next memasukkan kolom setelah itu kalau error dimasukkan ke dalam array
	for rows.Next(){
		err = rows.Scan(&obj.Donation_ID, &obj.Payment_method,  &obj.User_donation)

		if err != nil {
			return res, err
		}

		arrObj = append(arrObj, obj)
		
	}

	res.Status = http.StatusOK 
	res.Message = "Success"
	res.Data = arrObj

	return res, nil
}


//Read Count
func FetchCountDonation() (Response, error){
	var obj DonationSum
	var arrObj []DonationSum
	var res Response
//conn = connection
	con:= db.CreateCon()

	//untuk Get Mahasiswa
	sqlStatement := "SELECT SUM(`user_donation`) FROM `donation`" // ga pakai tanda petik sebelah angka 1 jg gpp

	rows, err := con.Query(sqlStatement)
	//perlu di close, biar ga banyak ke load dan biar ga lemot, karna kalau ga diputus dia kan kirim terus ga berhenti2 
	defer rows.Close()

	if err != nil {
		return res, err
	}
	//rows.next memasukkan kolom setelah itu kalau error dimasukkan ke dalam array
	for rows.Next(){
		err = rows.Scan(&obj.User_donation)
		if err != nil {
			return res, err
		}
		arrObj = append(arrObj, obj)
		
	}

	res.Status = http.StatusOK 
	res.Message = "Success"
	res.Data = arrObj

	return res, nil
}

//insert data
func StoreDonation(payment_method string, user_donation int)(Response, error){
	var res Response 

	v := validator.New()

	dns := Donation{
		Payment_method: payment_method,
		User_donation: user_donation,
	}
   
	err := v.Struct(dns)
	if err != nil {
	 res.Status = http.StatusBadRequest
	 res.Message = "Error"
	 res.Data = map[string]string{
		"errors": err.Error(),
	 }
	 return res, err
	}

	con := db.CreateCon()
	sqlStatement := "INSERT INTO `donation`(`payment_method`, `user_donation`) VALUES (?,?)" // tanda tanya itu masuk prepare statement

	stmt, err := con.Prepare(sqlStatement)
	if err != nil{
		res.Status = http.StatusInternalServerError
		res.Message = "Error"
		res.Data = map[string]string{
		 "errors": err.Error(),
		}
		return res, err
	   }

	//meng execute lalu terima parameter dan usahakan harus urut dengan yang diinsert kan, jadi nim,name,gender,fakultas,prodi
	result, err := stmt.Exec(payment_method,user_donation)

	if err != nil{
		res.Status = http.StatusInternalServerError
		res.Message = "Error"
		res.Data = map[string]string{
		 "errors": err.Error(),
		}
		return res, err
	   }
	//untuk mengecek sudah masuk apa tidak query nya
	lastInsertedID, err := result.LastInsertId()

	if err != nil {
		return res, err
	}

	res.Status = http.StatusOK 
	res.Message = "Success"
	res.Data = map[string] int64 {
		"last_insert_id": lastInsertedID, 
	}

	return res, nil

}

//update data
func UpdateDonation(donation_id int, payment_method string, user_donation int)(Response, error){
	var res Response 

	con := db.CreateCon()
	sqlStatement := "UPDATE `donation` SET payment_method=?, user_donation=? WHERE id=?" // tanda tanya itu masuk prepare statement
	stmt, err := con.Prepare(sqlStatement)

	if err != nil {
		return res, err
	}

	//meng execute lalu terima parameter dan usahakan harus urut dengan yang diinsert kan, jadi nim,name,gender,fakultas,prodi
	result, err := stmt.Exec(donation_id, payment_method,user_donation)

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
		"row_affected": rowAffected, 
	}

	return res, nil
}

//update data
func DeleteDonation(donation_id int)(Response, error){
	var res Response 

	con := db.CreateCon()
	sqlStatement := "DELETE FROM `donation` WHERE id=?" // tanda tanya itu masuk prepare statement

	stmt, err := con.Prepare(sqlStatement)

	if err != nil {
		return res, err
	}

	//meng execute lalu terima parameter dan usahakan harus urut dengan yang diinsert kan, jadi nim,name,gender,fakultas,prodi
	result, err := stmt.Exec(donation_id)

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
		"row_affected": rowAffected, 
	}

	return res, nil

}
