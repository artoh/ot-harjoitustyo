# Vaatimusmäärittely


## Sovelluksen käyttäjät

Sovelluksen avulla käyttäjä pystyy pitämään kirjaa eri kursseilla (tai muunlaisissa projekteissa) käyttämästään ajasta ja tallentamaan tuntikirjanpito markdown-tiedostoina

## Käyttäjät

Sovelluksessa on vain yksi käyttäjärooli (*käyttäjä*)

## Käyttöliittymäluonnos

Ohjelman pääikkunassa on taulukkoesitys tuntikirjanpidosta sekä mahdollisuudet suodattaa kirjauksia projektien mukaisesti.

Tehtävän kirjaamiselle ja aikaseurannalle on oma näkymänsä (tai vaihtoehtoisesti tämäkin sijoitetaan samalle näytölle)

Apudialogeja tarvittaneen ainakin uuden projektin lisäämiselle.

![Käyttöliittymä](kayttoliittyma.svg)

## Perusversion tarjoama toiminnallisuus

- Kun ohjelma käynnistyy, luetaan kaikkien projektien työaikakirjanpidot ja niistä muodostetaan käyttäjälle esitettävä yhteenveto **tehty**
- Käyttäjä voi tarkastella näkymää ajan mukaan **tehty** tai tarkastella yksittäisen projektin kirjauksia
- Käyttäjä voi lisätä projekteja (uusi työajanseuranta taikka olemassa olevan md-tiedoston tuominen) **md-tiedostot tehty**

- Käyttäjä tekee kirjauksen
  - syöttämällä päivämäärän, tunnit ja tehtävän tai **tehty**
  - käyttämällä ajanottopainikkeita tai
  - jatkamalla aiempaa tehtävää

- Käyttäjä voi korjata aiempia kirjauksia **tehty**


## Jatkokehitysideoita

- vaihtoehtoisia tallennusbackendeja (csv, excel, Google Calendar API)
- erilaiset ajankäytön raportit
