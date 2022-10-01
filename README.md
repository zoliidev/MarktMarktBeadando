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

> #### Validate-API
> https://oldal.vaganyzoltan.hu/api/validate
> POST-request-ben kell elküldeni:
> - token
> 
>Kimenet -> Json tábla:
> - loggedIN -> true || false
> - resp -> felhasználó számára olvasható hibaüzenet

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

> #### ProductList-API
> https://oldal.vaganyzoltan.hu/api/getProdList.php
> POST-request-ben kell elküldeni:
> - limit (hány termék jöjjön vissza egy lekéréssel)
> - offset (kezdőpont, pld ha 20 terméket lekértél akkor a 20-adiktól kezdjen)
> - token
>
>Kimenet -> Json tábla:
> - termék-id lista

>Tervezés: Figma
> - https://www.figma.com/file/LpYaS9CWV5I1zuZ5ZhE4kT/MarktMarkt-tervek?node-id=0%3A1
