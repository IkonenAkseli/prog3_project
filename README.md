# Dokumentaatio/loppudokumentti

## Ohjelman käyttö

Ohjelma vaatii tiedoston students.json kansioon src. Tästä tiedostosta luetaan ja siihen tallennetaan opiskelijoiden tiedot.

Ohjelma aukeaa valintasivulle, josta tulee valita joko opettaja- tai opiskelijaroolissa kirjautuminen.
Kumman tahansa valinnan jälkeen aukea näkymä, jossa tulee syöttää opiskelijan numero ja Nimi, jonka jälkeen pääsee ohjelman
päänäkymään. Opettaja ja opiskelijaroolin erona on, että opettaja voi lisätä suorituksia näkymästä, kun taas opiskelija ei.
Vain yhden oppilaan tiedot voivat olla valittuina kerralla. Siirtymällä takaisin alkuysivulle "takaisin" painikkeilla pääsee
valitsemaan uuden opiskelijan tiedot, joko opiskelija- tai opettajaroolissa. Jos opiskelijanumerolla löytyy eriniminen opiskelija
kuin alkusivulla syötetty, ohjelma valittaa siitä ja odottaa uutta numeroa/nimeä.

Päänäkymässä näkyy oletuksena kaikki tutkinto-ohjelmat, mutta niitä voi viltteröidä tason mukaan chekboxeilla ja/tai
tekstihaun avulla. Näytettävät tutkinto-ohjelmat päivitetään päivitä napin avulla, jos haluaa kaikki näkyville filtteröinnin
jälkeen, tulee painaa päivitä-nappia siten, että kaikki kentät ovat tyhjiä. Takaisin-nappi vie takaisin ja sulje-nappi lopettaa ohjelman toiminnan ja tallentaa muutokset src/students.json -tiedostoon, josta ohjelma opiskelijan tietoja valitessa hakee tiedot.
Tutkinto-ohjelman voi valita klikkaamalla sen nimeä, jonka jälkeen aukeaa puunäkymä. Jos tästä näkymästä menee takaisin alkunäkymään
voi vaihtaa opettaja- ja opiskelijaroolin välillä, sekä tulee syöttää uudelleen opiskelijan tiedot, jolloin opiskelijan vaihtaminen
on mahdollista.

Puunäkymässä näytetään koko tuktinto-ohjelman rakenne ja kurssit treeviewissä. Ikkunan oikealla reunalla on lista suorituksista ja
suunnitelmasta. Kurssin pääsee lisäämään/poistamaan suunnitelmasta klikkaamalla kurssia ja valitsemalla aukeavasta ikkunasta
haluttu toimenpide. Jos ohjelman alussa valittiin opettajarooli, voi kurssin tuossa ikkunassa lisätä tarkasteltavan opiskelijan
suorituksiin. Suunnitelmassa olevan kurssin lisääminen suorituksiin poistaa kurssin suunnitelmasta. Opiskelijan opintopisteet
näkyvät vasemmassa alareunassa. Ohjelma laskee vain valitun tuktinto-ohjelman suoritettujen kurssien opintopisteitä.


## Lisäominaisuudet

### Tutkinto-ohjelmien haku ja filtteröinti.
Ohjelman toisessa näkymnässä on mahdollista hakea ja/tai filtteröiudä näytettäviä
tukinto-ohjelmia. Tämä on vaatinut koodilisäyksiä metodiin, joka tallettaa tiedot apista, sekä omat käyttäliittymäasiansa,
sekä tietenkin filtteröinnin ja haun logiikan.

### Opettajarooli

Ohjelman alussa voi valita haluaako toimia opettajana vai oppilaana. Opettajaroolissa voi lisätä valitulle opiskelijalle kursseja.

### Luokkien vastuujako ja toiminta, toimintaa kuvattu myös koodin kommentoinnissa

#### App.java

Päävastuu: Akseli.

Jetro toteuttanut updateComboBox ja sen vaatimat GUI-elementit, Antti toteuttanut Opiskelijavalinnan GUI:n.
Akseli tehnyt loput.

Pyörittää käyttöliittymää ja sisältää paljon ohjelman toiminnna logiikkaa.


#### HandleApi.java

Päävastuu: Akseli.

Jetro toteuttanut bachDegrees, mastDegrees, doctDegrees ja miscDegrees filtteröinnin, tallettamisen ja filtteröinnin haetuista ohjelmista.
Akseli tehnyt loput.

Hoitaa tietojen hakemisen, prosessoinin ja tallentamisen.


#### Course.java

Kokonaan Akseli.

Mallintaa kurssia ja tallentaa sellaisen tiedot.

#### OpiskelijaAsetus.java

Päävastuu Antti.

Akseli tehnyt pieniä korjauksia ja parannuksia, muuten täysin Antti.

OpeskelijaAsetus.java luokan pääasiallinen tarkoitus on muokata ja välittää tietoa JSON-tiedoston, johon tallennetaan tiedot palvelun opiskelijoista 
sekä pääasiallisen toimintoluokan App.javan välillä. OpeskelijaAsetus.java tiedostoa kutsutaan ohjelman käynnistyessä, jolloin JSON-tiedosta ladataan
viimeisin versio opiskelijoiden tidoista ohjelmaan talteen, ohjelman ajon aikaista tiedon muokkaamista varten. Muokattua tietoa ei kirjoiteta JSON-tiedostoon,
enne ohjelman sulkemista, jolla vältetään turhaa tiedoston kirjoittamista/lukemista ajon aikana, jos/kun käyttäjä muuttaa tai peruu valintojaan. Luokka sisältää
6 pääasiallista metodia joita käytetään ajon aikana. 
    addStudent-metodi lisää uuden opiskelijan ohjelmaan talteen otettuun JSON-tietoon. metodi luo opiskelija annetun opiskelija numeron, sekä nimen perusteella,
sekä alustaa tyhjän listat suoritetuille ja suunnitetuille kursseille.
    addPlannedCourse-metodi lisää annetulle opiskelijalle uuden kurssin suunniteltujen kurssien säiliöön.
    addDoneCourse-metodi lisää opiskelijalle kurssin suoritettujen kurssien säiliöön, tämän voi tehdä vain opettaja-asetuksen ollessa päällä.
    removePlannedCourse-metodi poistaa opiskelijalta halutun kurssin suunnitellut kurssit säiliöstä.
    getStudenCourses-metodi hakee halutun opiskelijan kaikki kurssi suunnitellut/suoritettu ja palauttaa ne.
    getStudents-metodi kerää kaikki tiedostossa olevat opiskelijat ja heidän opiskelijanumeronsa ja palauttaa ne siististi treemap muodossa.

#### Yksikkötestit

Kokonaan Jetro.


#### Javadoc-kommentit

Kommentit generoitu netBeansilla, joten eivät taida mennä gittiin. Tämän voi replikoida ajamalla run generate javadoc.
Kommentit Kirjoitettu käyttäen samaa työnjakoa kuin koodin kirjoittamiseen.