package controllers

import (
	"net/http"
	"strconv"
	"time"
	"weacare_api/helpers"
	"weacare_api/models"

	"github.com/dgrijalva/jwt-go"
	"github.com/labstack/echo/v4"
)

func CheckLogin(c echo.Context) error {
	username := c.FormValue("username")
	password := c.FormValue("password")
	res, user, err := models.CheckLogin(username, password)

	if err != nil {
		return c.JSON(http.StatusInternalServerError, map[string]string{
			"message": err.Error(),
		})
	}

	if !res {
		return echo.ErrUnauthorized
	}

	token := jwt.New(jwt.SigningMethodHS256)
	claims := token.Claims.(jwt.MapClaims)
	claims["username"] = username
	claims["level"] = "application"
	claims["exp"] = time.Now().Add(time.Hour * 72).Unix()

	mytoken, err := token.SignedString([]byte("my-s3cr3t-k3Y"))

	if err != nil {
		return c.JSON(http.StatusInternalServerError, map[string]string{"message": err.Error()})
	}

	
	return c.JSON(http.StatusOK, map[string] string {
		"message": "Login successful",
		"token":   mytoken,
		"id": strconv.Itoa(user.Id),
		"username": user.Username,
		"email": user.Email,
	})
}

func GenerateHashPassword(c echo.Context) error {
	password := c.Param("password")
	hash, _ := helpers.HashPassword(password)

	return c.JSON(http.StatusOK, hash)
}

func StoreUser(c echo.Context) error {
	username := c.FormValue("username")
	password := c.FormValue("password")
	email := c.FormValue("email")

	result, err := models.StoreUser(username, password, email)
	if err != nil {
		return c.JSON(http.StatusInternalServerError, err.Error())
	}

	return c.JSON(http.StatusOK, result)
}

func UpdateUser(c echo.Context) error{
	id := c.FormValue("id")
	intId, err:= strconv.ParseInt(id, 0, 64)
	username := c.FormValue("username")
	password := c.FormValue("password")
	email := c.FormValue("email")
	
	result, err := models.Updateuser(int(intId), username, email, password)

	if err != nil{
		return c.JSON(http.StatusInternalServerError,
		map[string]string{"message": err.Error()}) 
	}

	return c.JSON(http.StatusOK, result)
}