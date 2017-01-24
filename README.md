# Parliament of Fools

([Referenca](https://www.youtube.com/watch?v=IVJkVCWXe9Q))

Aplikacija za vodjenje pravnih akata i amandmana. >.>

#### Projekat iz predmeta 'XML i Web servisi'.

#### Članovi

* Jasna Stanković, SW5/2013
* Nikola Lukić, SW21/2013

#### Pokretanje aplikacije

Mora postojati lokalna MySQL baza podataka sa korisnikom **root** koji ima šifru **root**, i u njoj šema **parliament**.
```sql
CREATE SCHEMA `poretti` DEFAULT CHARACTER SET utf8 ;
```

Takodje, mora postojati i lokalni MarkLogic server, sa bazom nazvanom **xml**, i korisnik **root** koji ima šifru **root** za pristup MarkLogic bazi.

Projekat uvući u Intellij IDEA-u, omogućiti __Annotation processing__ u podešavanjima projekta, napraviti Spring Boot konfiguraciju i pokrenuti.


