package routes

import (
	"net/http"
	"weacare_api/controllers"

	// "vp_week11_echo/middleware"

	"github.com/labstack/echo/v4"
)

// e.GET("/users/:id", getUser)
func getUser(c echo.Context) error {
	// User ID from path `users/:id`
	name := c.Param("name")
  return c.String(http.StatusOK, "Hello, " + name)
}


func Init() *echo.Echo {
	e := echo.New()
	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, gais!")
	})

	e.GET("/user", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, this is user page!")
	})


	//inisialisasi yg berhubungan dg database ada config, controllers. 
	e.GET("/user/:name", getUser) 

	 // /mahasiswa ini manggil controllers.mahasiswa 
	//  e.GET("/donation", controllers.FetchAllDonation, middleware.IsAuthenticated)
	e.GET("/donation", controllers.FetchAllDonation)
	e.GET("/donation/count", controllers.FetchSumDonation)
	e.POST("/donation", controllers.StoreDonation)
	e.PATCH("/donation", controllers.UpdateDonation)
	e.DELETE("/donation", controllers.DeleteDonation)

	e.GET("/generate-hash/:password", controllers.GenerateHashPassword)
	e.POST("/register", controllers.StoreUser)
	e.POST("/login", controllers.CheckLogin)
	e.PUT("/updateuser", controllers.UpdateUser)
	e.POST("/test-validation", controllers.TestStructValidation)
	e.POST("/test-validation-var", controllers.TestVarValidation)

	return e
}
