# LaskeTunnit

Sovelluksen avulla käyttäjä voi pitää (esimerkiksi Ohjemistotekniikan menetelmät -kurssilla vaadittua) työaikakirjanpitoa.

Sovellukseen on mahdollistaa toteuttaa tuki erilaisille tiedostomuodoille tai muille tallennusratkaisuille. Aluksi ohjelma tukee työaikakirjanpidon tallentamista markdown-muotoon.


## Dokumentaatio

- [Vaatimusmaarittely](dokumentointi/vaatimusmaarittely.md)
- [Arkkitehtuurikuvaus](dokumentointi/arkkitehtuuri.md)
- [Työaikakirjanpito](dokumentointi/tuntikirjanpito.md)


## Komentorivitoiminnot

### Ohjelman suorittaminen

```
mvn compile exec:java -Dexec.mainClass=artoh.lasketunnit.ui.LaskeTunnitApplication
```

### Testaus

Testien suorittaminen
```
mvn test
```

Testikattavuusraportin luominen
```
mvn jacoco:report
```
Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto target/site/jacoco/index.html


### JavaDoc
JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```
JavaDocia voi tarkastella avaamalla selaimella tiedosto target/site/apidocs/index.html

### Checkstyle

Tiedostoon [checkstyle.xml](https://github.com/artoh/ot-harjoitustyo/blob/master/LaskeTunnit/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto target/site/checkstyle.html
