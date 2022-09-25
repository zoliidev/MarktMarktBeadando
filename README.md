# MarktMartktBeadando / DROID APP
**Classification: internal & partners only**

# Api Segéd-software: POSTMAN!
> #### Login-API
> https://oldal.vaganyzoltan.hu/api/login.php
> POST-request-ben kell elküldeni:
> - name (username)
> - password
> - token (ha van)
> - deviceName
>
>Kimenet -> Json tábla:
> - JsonWebToken (30 napos lejárati idővel)

> #### Product-API
> https://oldal.vaganyzoltan.hu/api/product.php
> POST-request-ben kell elküldeni:
> - termékkód
> - token
>
>Kimenet -> Json tábla:
> - termék neve
> - termék ára
> - termék leírása
> - termék képe (a webszerverre mutat azaz: https://oldal.vaganyzoltan.hu/prod-img/<IMG változó>

>Tervezés: Figma
> - https://www.figma.com/file/LpYaS9CWV5I1zuZ5ZhE4kT/MarktMarkt-tervek?node-id=0%3A1
