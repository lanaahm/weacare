package controllers

import (
	"net/http"
	"strconv"
	"weacare_api/models"

	"github.com/labstack/echo/v4"
)

func FetchAllDonation(c echo.Context) error{

	result, err := models.FetchAllDonation()

	if err != nil{
		return c.JSON(http.StatusInternalServerError,
		map[string]string{"message": err.Error()}) //eror nya kan objek supaya bisa masuk ke json itu dikasih string line, 
	} 											  //tapi kalau gada eror langsung return statusnya ok result nya apa

	return c.JSON(http.StatusOK, result)
}

func FetchSumDonation(c echo.Context) error{

	result, err := models.FetchCountDonation()

	if err != nil{
		return c.JSON(http.StatusInternalServerError,
		map[string]string{"message": err.Error()}) //eror nya kan objek supaya bisa masuk ke json itu dikasih string line, 
	} 											  //tapi kalau gada eror langsung return statusnya ok result nya apa

	return c.JSON(http.StatusOK, result)
}

func StoreDonation(c echo.Context) error{
	payment_method := c.FormValue("payment_method")
	user_donation := c.FormValue("user_donation")
	intuser_donation, err := strconv.ParseInt(user_donation,0,64)

	result, err := models.StoreDonation(payment_method, int(intuser_donation))

	if err != nil{
		return c.JSON(http.StatusInternalServerError,result)//eror nya kan objek supaya bisa masuk ke json itu dikasih string line, 
	} 											  //tapi kalau gada eror langsung return statusnya ok result nya apa

	return c.JSON(http.StatusOK, result)
}

func UpdateDonation(c echo.Context) error{

	donation_id := c.FormValue("donation_id")
	intdonation, err:= strconv.ParseInt(donation_id, 0, 64)
	payment_method := c.FormValue("payment_method")
	user_donation := c.FormValue("user_donation")
	intuser_donation, err := strconv.ParseInt(user_donation,0,64)
	result, err := models.UpdateDonation(int(intdonation), payment_method, int(intuser_donation))
	if err != nil{
		return c.JSON(http.StatusInternalServerError,
		map[string]string{"message": err.Error()}) //eror nya kan objek supaya bisa masuk ke json itu dikasih string line, 
	} 											  //tapi kalau gada eror langsung return statusnya ok result nya apa

	return c.JSON(http.StatusOK, result)
}

func DeleteDonation(c echo.Context) error{

	donation_id := c.FormValue("donation_id")
	intdonation, err:= strconv.ParseInt(donation_id, 0, 64)

	result, err := models.DeleteDonation(int(intdonation))

	if err != nil{
		return c.JSON(http.StatusInternalServerError,
		map[string]string{"message": err.Error()}) //eror nya kan objek supaya bisa masuk ke json itu dikasih string line, 
	} 											  //tapi kalau gada eror langsung return statusnya ok result nya apa

	return c.JSON(http.StatusOK, result)
}