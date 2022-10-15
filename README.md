# MarktMartktBeadando / DROID APP
**Classification: internal & partners only**

> #### Markt Markt android webshop
> Funkciók a felhasználó számára:
> - Fiókba történő bejelentkezés (automatikus opcionálisan)
> - Termékek kedvenchez hozzáadása
> - Termékek kosárhoz hozzáadása
> - Kosárból történő megrendelés
> - Téma változtatás
> - Jelszóváltoztatás
> - Kijelentkezés

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

> #### PasswordChange-API
> https://oldal.vaganyzoltan.hu/api/passChange.php
> POST-request-ben kell elküldeni:
> - régi jelszó
> - új jelszó
> - token
>
>Kimenet -> Json tábla:
> - válaszüzenet (sikeres vagy nem)

> #### Termékkeresés-API
> https://oldal.vaganyzoltan.hu/api/search.php
> POST-request-ben kell elküldeni:
> - keresni kívánt termék
> - token
>
>Kimenet -> Json tábla:
> - max 10 elemű terméklista

> #### Kosár & kedvenc termékek-API
> https://oldal.vaganyzoltan.hu/api/listFav.php || https://oldal.vaganyzoltan.hu/api/listCart.php
> POST-request-ben kell elküldeni:
> - token
>
>Kimenet -> Json tábla:
> - Felhasználó terméklistája

> #### Kosár & kedvenc termék hozzáadása/eltávolítása-API
> https://oldal.vaganyzoltan.hu/api/addFav.php || https://oldal.vaganyzoltan.hu/api/addCart.php
> Kezeli a hozzáadást, törlést. Ha a termék már szerepel a listán eltávolítja.
> POST-request-ben kell elküldeni:
> - id (termékkód)
> - token
>
>Kimenet -> Json tábla:
> - Frissített terméklista

> #### Akciós termékek listája-API
> https://oldal.vaganyzoltan.hu/api/getDiscounted.php
> POST-request-ben kell elküldeni:
> - token
>
>Kimenet -> Json tábla:
> - Akciós termékek listája


>Tervezés: Figma
> - https://www.figma.com/file/LpYaS9CWV5I1zuZ5ZhE4kT/MarktMarkt-tervek?node-id=0%3A1
