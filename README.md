# Dokumentaatio/loppudokumentti löytyy readme.txt tiedostosta

## Ohjelman käyttö

Ohjelma aukeaa valintasivulle, josta tulee valita joko opettaja- tai opiskelijaroolissa kirjautuminen.
Kumman tahansa valinnan jälkeen aukea näkymä, jossa tulee syöttää opiskelijan numero ja Nimi, jonka jälkeen pääsee ohjelman
päänäkymään. Opettaja ja opiskelijaroolin erona on, että opettaja voi lisätä suorituksia näkymästä, kun taas opiskelija ei.
Vain yhden oppilaan tiedot voivat olla valittuina kerralla. Siirtymällä takaisin alkuysivulle "takaisin" painikkeilla pääsee
valitsemaan uuden opiskelijan tiedot, joko opiskelija- tai opettajaroolissa. Jos opiskelijanumerolla löytyy eriniminen opiskelija
kuin alkusivulla syötetty, ohjelma valittaa siitä ja odottaa uutta numeroa/nimeä.

Päänäkymässä näkyy oletuksena kaikki tutkinto-ohjelmat, mutta niitä voi viltteröidä tason mukaan chekboxeilla ja/tai suorittaa
haun tekstikentän avulla. Näytettävät tutkinto-ohjelmat päivitetään päivitä napin avulla, jos haluaa kaikki näkyville filtteröinnin
jälkeen, tulee painaa päivitä-nappia siten, että kaikki kentät ovat tyhjiä. Takaisin-nappi vie takaisin ja sulje-nappi lopettaa ohjelman toiminnan ja tallentaa muutokset src/students.json -tiedostoon, josta ohjelma opiskelijan tietoja valitessa hakee tiedot.
Tutkinto-ohjelman voi valita klikkaamalla sen nimeä, jonka jälkeen aukeaa puunäkymä. Jos tästä näkymästä menee takaisin alkunäkymään
voi vaihtaa opettaja- ja opiskelijaroolin välillä, sekä tulee syöttää uudelleen opiskelijan tiedot, jolloin opiskelijan vaihtaminen
on mahdollista.

Puunäkymässä näytetään koko tuktinto-ohjelman rakenne ja kurssit treeviewissä. Ikkunan oikealla reunalla on lista suorituksista ja
suunnitelmasta. Kurssin pääsee lisäämään/poistamaan suunnitelmasta klikkaamalla kurssia ja valitsemalla aukeavasta ikkunasta
haluttu toimenpide. Jos ohjelman alussa valittiin opettajarooli, voi kurssin tuossa ikkunassa lisätä tarkasteltavan opiskelijan
suorituksiin. Suunnitelmassa olevan kurssin lisääminen suorituksiin poistaa kurssin suunnitelmasta.


