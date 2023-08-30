package controllers

import (
	"net/http"
	"weacare_api/models"

	"github.com/go-playground/validator"
	"github.com/labstack/echo/v4"
)

type User struct {
	Nama  string `json:"nama" validate:"required"`
	Email string `validate:"required,email"`
}

func TestStructValidation(c echo.Context) error {
	v := validator.New()

	usr := User{
		Nama:  "salsa",
		Email: " ",
	}

	err := v.Struct(usr)
	if err != nil {
		return c.JSON(
			http.StatusBadRequest,
			map[string]string{
				"message": err.Error(),
			})
	}
	return c.JSON(http.StatusOK,
		map[string]string{
			"message": "Success",
		})
}

func TestVarValidation(c echo.Context) error {
	var res models.Response
	v := validator.New()
	nama := " "
	email := " "
   
	var errordata = make(map[string]string)
   
	err1 := v.Var(email, "required,email")
	if err1 != nil {
	 errordata["email"] = "Email not valid."
	}
   
	err2 := v.Var(nama, "required")
	if err2 != nil {
	 errordata["name"] = "Name is required."
	}
   
	
   
	if len(errordata) != 0 {
	 res.Status = http.StatusBadRequest
	 res.Message = "Error"
	 res.Data = errordata
	 return c.JSON(http.StatusBadRequest, res)
	}
   
	return c.JSON(http.StatusOK,
	 map[string]string{
	  "message": "Success",
	 })
   }