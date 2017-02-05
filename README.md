# DE-IK PTI-MSc - Fejlett adatbázis technológiák - 2016

## Fejlesztők
* Czuczor Gergő
* Kiss Sándor Ádám
* Salagvárdi Tibor

### Windows-ra vonatkozó telepítési és használati útmutató

#### Rendszerkövetelmények

##### Alkalmazások

* BaseX 8.6
* Oracle JDK 8
* Apache Maven 3.3.9

##### Környezeti változók

* JAVA_HOME
* M2_HOME

Ezeket a PATH környezeti változóhoz is hozzá kell fűzni az alábbi módon:

* %JAVA_HOME%\bin
* %M2_HOME%\bin

Ezek ellenőrzése az alábbi módon történhet:

```
echo %JAVA_HOME%
C:\Program Files\Java\jdk1.8.0_121
```

```
echo %M2_HOME%
D:\apache-maven-3.3.9
```

#### BaseX adatbázisszerver telepítése

* Töltsük le a BaseX 8.6 adatbázisszerver telepítőjét: http://files.basex.org/releases/8.6/BaseX86.exe
* Telepítstük fel a letöltött adatbázisszervert.
* Indítsuk el a BaseX szervert: C:\Program Files (x86)\BaseX\bin\basexserver.bat
* Indítsuk el a BaseX klienst: C:\Program Files (x86)\BaseX\bin\basexclient.bat
* Jelentkezzünk be "admin/admin" felhasználónév/jelszó párossal.
* Másoljuk át a universe\src\main\resources\fejlett_adatbazis_hazi.xml állományt a D:\ meghajtó gyökerébe.
* A BaseX kliensen belül hozzuk létre az adatbázis sémát az alábbi parancs kiadásával: ```CREATE DB universe D:\fejlett_adatbazis_hazi.xml```
* Zárjuk be a BaseX klienst.

#### Az alkalmazás lefordítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe-model```
* ```mvn clean install```
* ```cd ..\universe```
* ```mvn clean install```

#### Az alkalmazás elindítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe```
* ```mvn clean package exec:java```

##Választott terület:
Az univerzumot alkotó entitásokat, tehát galaxisokat, naprendszereket, üstökösöket, bolygókat valamint holdakat tartalmazó adatbázis lett projektként választva.
##Az adatmodell leírása:
*Az xml gyökéreleme a "universe", maga az univerzum, mely tartalmazza a "galaxies" elemet.
Ezekből kötelezően egy elem található a fában, tehát létezniük kell és nem lehet több belőlük.
*A "galaxies" elem több "galaxy" elemet tartalmazhat, de ebből már nem kell egynek sem szerepelnie az xml-ben.
*Egy "galaxy" elem tartalmaz mindig egy "solarSystems" elemet, mely a naprendszereket tartalmazza. Ha létezik "galaxy" elem akkor abban kell szerepelnie egy "solarSystems" elemnek, de ebből is csak egy lehet.
*A "solarSystems" elem tartalmazza a naprendszereket leíró "solarSystem" elemeket, melyekből nem kötelező megadni egyet sem, illetve több is szerepelhet belőlük.
*A "solarSystem" elem tartalmazza a csillagot leíró "star" elem, mely kötelező és csak egy lehet belőle egy naprendszeren belül. Ez az elem két attribútummal rendelkezik, melyek a "name" és a "type". A "name" az egy karakterlánc, mely a csillag nevét jelöli. A "type" egy enumeráció, ami a csillag típusát írja le. A "type" attribútum a következő értékeket veheti fel.
"unary", "binary", "ternary" és "multiple".
*A "solarSystem" elem tartalmazza továbbá a "planets" elemet, mely szintén kötelező és csak egy lehet belőle egy naprendszeren belül.
A "planets" elem tartalmazza a "planet" elemeket, melyek a bolygók leírására szolgálnak.
Ezeknek a megadása nem kötelező, valamint több "planet" is lehet egy "planets" elemen belül.
*A "planet" a következő elemeket tartalmazza a bolygók leírására: "radius", "orbitalPeriod", "orbitalSpeed", "eccentricity", "semiMajorAxis", "mass" valamint "moons".
Ezek rendre a bolygó sugara, keringési ideje, keringési sebessége, különcsége, fél-főtengely, "tömeg" és a holdjai.
*A "moons" kivételével, ezek mind mennyiségek, egy attribútummal és szöveges tartalommal rendelkeznek. Ez az attribútumuk pedig a "unit", ami a mértékegységet írja le.
A "moons" elemből bolygónként kötelezően egy darab van és ez tartalmazza a "moon" elemeket, melyek a holdakat írják le.
*Egy "moon" elem tartalmaz egy "radius" elemet, mely a sugarát tárolja. Ez is mint a bolygók leíró elemei, rendelkezik egy "unit" nevű attribútummal, mely a mértékegységet határozza meg, valamint az elem szöveges tartalma fogja a mennyiséget tárolni, táhat a sugár szám szerinti értékét.
*A "solarSystem" a "planets"-en kívül még tartalmazza a "comets" elemet, melyből szintén kötelezően egy van minden naprendszerben.
*A "comets"-en belül "comet" elemek találhatók, melyek az üstökösöket írják le és a további további elemekkel rendelkeznek: "orbitalPeriod" és "minerals". Az "orbitalPeriod" ugyanazokkal a tulajdonságokkal rendelkezik mint a "planet" elemen belüli "orbitalPeriod".
*A "minerals" elemből kötelezően egy van üstökösönként. Ezek "mineral" elemeket tartalmaznak, melyek "quantity"-ket. A "mineral" elem nevezi meg az üstökösön található ásványi anyagokat, és a "quantity"-ben kerül tárolásra az adott anyag mennyisége, a mértékegységével együtt. Az adott mértékegység a "unit" attribútum segítségével nevezhető meg.
*A legtöbb elem tartalmaz egy "name" attribútumot, melyeknek megadása legtöbb esetben kötelező, hiszen kulcsként szolgálnak az adatbázisban.
*Ezeket a megszorításokat bővebben a mellékelt sémában találhatjuk meg, mely a projekt könyvtárszerkezetén belül a /universe/src/main/resources/universe.xsd fájlban található.

# Linkek:

http://docs.basex.org/wiki/XQuery_3.0

http://docs.basex.org/wiki/Validation_Module#XML_Schema_Validation

