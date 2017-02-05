# DE-IK PTI-MSc - Fejlett adatbázis technológiák - 2016

## Fejlesztők
* Czuczor Gergő
* Kiss Sándor Ádám
* Salagvárdi Tibor

## Windows-ra vonatkozó telepítési és használati útmutató

### Rendszerkövetelmények

#### Alkalmazások

* BaseX 8.6
* Oracle JDK 8
* Apache Maven 3.3.9

#### Környezeti változók

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

### BaseX adatbázisszerver telepítése

* Töltsük le a BaseX 8.6 adatbázisszerver telepítőjét: http://files.basex.org/releases/8.6/BaseX86.exe
* Telepítstük fel a letöltött adatbázisszervert.
* Indítsuk el a BaseX szervert: C:\Program Files (x86)\BaseX\bin\basexserver.bat
* Indítsuk el a BaseX klienst: C:\Program Files (x86)\BaseX\bin\basexclient.bat
* Jelentkezzünk be "admin/admin" felhasználónév/jelszó párossal.
* Másoljuk át a universe\src\main\resources\fejlett_adatbazis_hazi.xml állományt a D:\ meghajtó gyökerébe.
* A BaseX kliensen belül hozzuk létre az adatbázis sémát az alábbi parancs kiadásával: ```CREATE DB universe D:\fejlett_adatbazis_hazi.xml```
* Zárjuk be a BaseX klienst.

### Az alkalmazás lefordítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe-model```
* ```mvn clean install```
* ```cd ..\universe```
* ```mvn clean install```

### Az alkalmazás elindítása

Windows parancssorban navigáljunk el a projekt főkönyvtárába, majd adjuk ki egymás után az alábbi parancsokat:
* ```cd universe```
* ```mvn clean package exec:java```

## Választott terület:
Az univerzumot alkotó entitásokat, tehát galaxisokat, naprendszereket, üstökösöket, bolygókat valamint holdakat tartalmazó adatbázis lett projektként választva.

## Az adatmodell leírása:
* Az xml gyökéreleme a "universe", maga az univerzum, mely tartalmazza a "galaxies" elemet.
Ezekből kötelezően egy elem található a fában, tehát létezniük kell és nem lehet több belőlük.
* A "galaxies" elem több "galaxy" elemet tartalmazhat, de ebből már nem kell egynek sem szerepelnie az xml-ben.
*Egy "galaxy" elem tartalmaz mindig egy "solarSystems" elemet, mely a naprendszereket tartalmazza. Ha létezik "galaxy" elem akkor abban kell szerepelnie egy "solarSystems" elemnek, de ebből is csak egy lehet.
* A "solarSystems" elem tartalmazza a naprendszereket leíró "solarSystem" elemeket, melyekből nem kötelező megadni egyet sem, illetve több is szerepelhet belőlük.
* A "solarSystem" elem tartalmazza a csillagot leíró "star" elem, mely kötelező és csak egy lehet belőle egy naprendszeren belül. Ez az elem két attribútummal rendelkezik, melyek a "name" és a "type". A "name" az egy karakterlánc, mely a csillag nevét jelöli. A "type" egy enumeráció, ami a csillag típusát írja le. A "type" attribútum a következő értékeket veheti fel.
"unary", "binary", "ternary" és "multiple".
* A "solarSystem" elem tartalmazza továbbá a "planets" elemet, mely szintén kötelező és csak egy lehet belőle egy naprendszeren belül.
A "planets" elem tartalmazza a "planet" elemeket, melyek a bolygók leírására szolgálnak.
Ezeknek a megadása nem kötelező, valamint több "planet" is lehet egy "planets" elemen belül.
* A "planet" a következő elemeket tartalmazza a bolygók leírására: "radius", "orbitalPeriod", "orbitalSpeed", "eccentricity", "semiMajorAxis", "mass" valamint "moons".
Ezek rendre a bolygó sugara, keringési ideje, keringési sebessége, különcsége, fél-főtengely, "tömeg" és a holdjai.
* A "moons" kivételével, ezek mind mennyiségek, egy attribútummal és szöveges tartalommal rendelkeznek. Ez az attribútumuk pedig a "unit", ami a mértékegységet írja le.
A "moons" elemből bolygónként kötelezően egy darab van és ez tartalmazza a "moon" elemeket, melyek a holdakat írják le.
*Egy "moon" elem tartalmaz egy "radius" elemet, mely a sugarát tárolja. Ez is mint a bolygók leíró elemei, rendelkezik egy "unit" nevű attribútummal, mely a mértékegységet határozza meg, valamint az elem szöveges tartalma fogja a mennyiséget tárolni, táhat a sugár szám szerinti értékét.
* A "solarSystem" a "planets"-en kívül még tartalmazza a "comets" elemet, melyből szintén kötelezően egy van minden naprendszerben.
* A "comets"-en belül "comet" elemek találhatók, melyek az üstökösöket írják le és a további további elemekkel rendelkeznek: "orbitalPeriod" és "minerals". Az "orbitalPeriod" ugyanazokkal a tulajdonságokkal rendelkezik mint a "planet" elemen belüli "orbitalPeriod".
* A "minerals" elemből kötelezően egy van üstökösönként. Ezek "mineral" elemeket tartalmaznak, melyek "quantity"-ket. A "mineral" elem nevezi meg az üstökösön található ásványi anyagokat, és a "quantity"-ben kerül tárolásra az adott anyag mennyisége, a mértékegységével együtt. Az adott mértékegység a "unit" attribútum segítségével nevezhető meg.
* A legtöbb elem tartalmaz egy "name" attribútumot, melyeknek megadása legtöbb esetben kötelező, hiszen kulcsként szolgálnak az adatbázisban.
* Ezeket a megszorításokat bővebben a mellékelt sémában találhatjuk meg, mely a projekt könyvtárszerkezetén belül a /universe/src/main/resources/universe.xsd fájlban található.

## Linkek:

http://docs.basex.org/wiki/XQuery_3.0

http://docs.basex.org/wiki/Validation_Module#XML_Schema_Validation

### Lekérdezések

UniverseServiceImpl

* findAllGalaxies
        Lekérdezi az összes galaxist az adatbázisból.
        
        declare variable $dbName external;
        for $galaxy in db:open($dbName)//galaxies/* return $galaxy
        
* cometsThatHaveMoreThanOneMineralOrderedByQuantitySumDesc
        Lekérdezi az üstökösöket, amelyeken több mint egy ásványi anyag található, a mennyiségek összege alapján csökkenő sorrendbe rendezve.
        
        declare variable $dbName external;
        for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet
        let $mineralCount := count($comet/minerals/mineral)
        let $quantitySum := sum(
        if($comet/minerals/mineral/quantity/@unit = 'kg') then
            $comet/minerals/mineral/quantity * 1000
        else
            $comet/minerals/mineral/quantity
        )
        where $mineralCount > 1
        order by $quantitySum descending
        return $comet
        
* avgOrbitalSpeedOfPlanetsThatHaveMoonsWithRadiusBetween
        Azon bolygók átlagos keringési sebessége, amelyeknek van olyan holdja, aminek a sugara a paraméterekben megadott sugarak között van.
        
        declare variable $dbName external;
        declare variable $lowerBound external;
        declare variable $upperBound external;
        let $avg := avg(
        for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet
            let $orbSpeed := 
                if ($planet/orbitalSpeed/@unit = 'mps') then
                    $planet/orbitalSpeed * 3.6
                else if ($planet/orbitalSpeed/@unit = 'kps') then
                    $planet/orbitalSpeed * 3600
                else
                    $planet/orbitalSpeed
            for $moon in $planet/moons/moon
                let $moonRadius := 
                    if ($moon/radius/@unit = 'm') then
                        $moon/radius * 0.001
                    else if ($moon/radius/@unit = 'SolarRadius') then
                        $moon/radius * 695700
                    else
                        $moon/radius
            where $moonRadius >= $lowerBound and $moonRadius <= $upperBound
            return $orbSpeed
        )
        return $avg
        
* findSmallPlanetsGroupedBySolarSystem
    
        A kis bolygókat kérdezi le naprendszerenként csoportosítva.
        
        declare variable $dbName external;
        declare variable $upperBound external;
        for $solarSystem in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem
        let $name := $solarSystem/@name
        for $planet in $solarSystem/planets/planet
            let $radius := 
                if ($planet/radius/@unit = 'm') then
                    $planet/radius * 0.001
                else if ($planet/radius/@unit = 'SolarRadius') then
                    $planet/radius * 695700
                else
                    $planet/radius
        where $radius <= $upperBound
        group by $name
        return element solarSystem {
        attribute name {$name},
        element planets {$planet}
        }
    
* findAllSolarSystemsInGalaxy
    
        Az összes naprendszert kérdezi le egy galaxisból.
        
        declare variable $dbName external;
        declare variable $name external;
        for $solarSystem in db:open($dbName)//galaxies/galaxy[@name=$name]/solarSystems/*
        return $solarSystem
    
* findAllPlanetsInSolarSystem
    
        Lekérdezi az össze bolygót a naprendszerből.
        
        declare variable $dbName external;
        declare variable $name external;
        for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/planets/planet
        return $planet
    
* findAllCometsInSolarSystem
        
        Lekérdezi az összes üstököst egy naprendszerből.
        
        declare variable $dbName external;
        declare variable $name external;
        for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/comets/comet
        return $comet
    
* findAllMoonsAroundPlanet
    
        Az egy bolygóhoz tartozó holdakat kérdezi le.
        
        declare variable $dbName external;
        declare variable $name external;
        for $moon in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]/moons/moon
        return $moon
    
* findAllMineralsInComet
    
        Lekérdezi az üstökösön lévő nyersanyagokat.
        
        declare variable $dbName external;
        declare variable $name external;
        for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]/minerals/mineral
        return $mineral
        
* findPlanetByName
        
        Neve alapján kérdezi le a bolygót.
        
        declare variable $dbName external;
        declare variable $name external;
        for $planet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]
        return $planet
        
* findMoonByName
        
        Neve alapján kérdezi le a holdat.
        
        declare variable $dbName external;
        declare variable $name external;
        for $moon in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$name]
        return $moon
    
* findCometByName
        
        Neve alapján kérdezi le az üstököst.
        
        declare variable $dbName external;
        declare variable $name external;
        for $comet in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]
        return $comet
    
* findStarInSolarSystem
        
        Adott naprendszeren belül kérdezi le a csillagot.
    
        declare variable $dbName external;
        declare variable $name external;
        for $star in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]/star
        return $star
    
* findMineralByComet
        
        Lekérdez egy adott ásványi anyagot egy adott üstökösről.
        
        declare variable $dbName external;
        declare variable $cometName external;
        declare variable $mineralName external;
        for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]
        return $mineral
    
* findCometBySolarSystem
        
        Lekérdez egy adott üstököst egy adott naprendszerből.
        
        declare variable $dbName external;
        declare variable $solarSystemName external;
        declare variable $cometName external;
        for $mineral in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/comets/comet[@name=$cometName]
        return $mineral
    
* findSolarSystemByName
        
        Név alapján kérdezi le a naprendszert.
        
        declare variable $dbName external;
        declare variable $name external;
        for $solarSystem in db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]
        return $solarSystem
    
* findGalaxyByName
        
        Név alapján kérdezi le a galaxist.
        
        declare variable $dbName external;
        declare variable $name external;
        for $solarSystem in db:open($dbName)//galaxies/galaxy[@name=$name]
        return $solarSystem
    
* deleteMineralOnComet
    
        Törli az adott ásványi anyagot az adott üstökösről.
        
        declare variable $dbName external;
        declare variable $cometName external;
        declare variable $mineralName external;
        delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName]
    
* deleteComet
    
        Törli az adott üstököst.
        
        declare variable $dbName external;
        declare variable $name external;
        delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$name]
        
* deleteMoon
    
        Törli az adott holdat.
        
        declare variable $dbName external;
        declare variable $name external;
        delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$name]
    
* deletePlanet
    
        Törli az adott bolygót.
        
        declare variable $dbName external;
        declare variable $name external;
        delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$name]
    
* deleteSolarSystem
    
        Törli az adott naprendszert.
        
        declare variable $dbName external;
        declare variable $name external;
        delete nodes db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$name]
    
* deleteGalaxy
        
        Törli az adott galaxist.
        
        declare variable $dbName external;
        declare variable $name external;
        delete nodes db:open($dbName)//galaxies/galaxy[@name=$name]
    
* updatePlanetRadius
    
        Módosítja az adott bolygó sugarát.
        
        declare variable $dbName external;
        declare variable $planetName external;
        declare variable $newPlanet external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName] with $newPlanet
    
* updateMoonRadius
    
        Módosítja az adott hold sugarát.
        
        declare variable $dbName external;
        declare variable $moonName external;
        declare variable $newMoon external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet/moons/moon[@name=$moonName] with $newMoon
    
* updateMineralOnComet
    
        Módosítja az adott nyersanyagot az adott üstökösön.
        
        declare variable $dbName external;
        declare variable $cometName external;
        declare variable $mineralName external;
        declare variable $newMineral external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals/mineral[@elementName=$mineralName] with $newMineral
        
* updateCometOrbitalPeriod
    
        Módosítja az üstökös keringési periódusát.
        
        declare variable $dbName external;
        declare variable $cometName external;
        declare variable $newComet external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName] with $newComet
    
* updateComet
    
        Módosítja az adott üstököst.
        
        declare variable $dbName external;
        declare variable $oldCometName external;
        declare variable $newComet external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$oldCometName] with $newComet
    
* updateStarInSolarSystem
        
        Módosítja a csillagot a naprendszerben.
        
        declare variable $dbName external;
        declare variable $solarSystemName external;
        declare variable $newStar external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/star with $newStar
    
* updatePlanet
    
        Módosítja az adott bolygót.
        
        declare variable $dbName external;
        declare variable $planetName external;
        declare variable $newPlanet external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName] with $newPlanet
    
* updateMoonForPlanet
        
        Módosítja az adott bolygóhoz tartozó holdat.
        
        declare variable $dbName external;
        declare variable $planetName external;
        declare variable $moonName external;
        declare variable $newMoon external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName]/moons/moon[@name=$moonName] with $newMoon
    
* updateGalaxy
    
        Módosítja az adott galaxist.
        
        declare variable $dbName external;
        declare variable $oldGalaxyName external;
        declare variable $newGalaxy external;
        replace node db:open($dbName)//galaxies/galaxy[@name=$oldGalaxyName] with $newGalaxy
        
* updateSolarSystem
    
        Módosítja az adott naprendszert.
        
        declare variable $dbName external;
        declare variable $oldSolarSystemName external;
        declare variable $newSolarSystem external;
        replace node db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$oldSolarSystemName] with $newSolarSystem
    
* addPlanetToSolarSystem
    
        Hozzáadja az adott bolygót az adott naprendszerhez.
        
        declare variable $dbName external;
        declare variable $solarSystemName external;
        declare variable $newPlanet external;
        insert node ($newPlanet) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/planets
    
* addMoonToPlanet
    
        Hozzáadja az adott holdat az adott bolygóhoz.
        
        declare variable $dbName external;
        declare variable $planetName external;
        declare variable $newMoon external;
        insert node ($newMoon) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/planets/planet[@name=$planetName]/moons
    
* addMineralToComet
    
        Hozzáadja az adott ásványi anyagot az adott üstököshöz.
        
        declare variable $dbName external;
        declare variable $cometName external;
        declare variable $newMineral external;
        insert node ($newMineral) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem/comets/comet[@name=$cometName]/minerals
    
* addCometToSolarSystem
    
        Hozzáadja az adott üstököst az adott naprendszerhez.
        
        declare variable $dbName external;
        declare variable $solarSystemName external;
        declare variable $newComet external;
        insert node ($newComet) into db:open($dbName)//galaxies/galaxy/solarSystems/solarSystem[@name=$solarSystemName]/comets
    
* addSolarSystemToGalaxy
    
        Hozzáadja az adott naprendszert az adott galaxishoz.
        
        declare variable $dbName external;
        declare variable $galaxyName external;
        declare variable $newSolarSystem external;
        insert node ($newSolarSystem) into db:open($dbName)//galaxies/galaxy[@name=$galaxyName]/solarSystems
    
* addGalaxyToUniverse
        
        Hozzáadja a galaxist az univerzumhoz.
        
        declare variable $dbName external;
        declare variable $newGalaxy external;
        insert node ($newGalaxy) into db:open($dbName)//galaxies
