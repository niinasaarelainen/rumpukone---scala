import scala.collection.mutable.Buffer
import scala.collection.mutable.Map

 trait ViivastolleLaitettava {  
  
  var viivasto = Buffer[String]()   
  val y = Map("lyr" -> 16, "alatila" ->15, "c1" -> 14, "d1" -> 13,  "e1" -> 12,  "f1" -> 11,  "g1"-> 10,  "a1"->9,  "h1" -> 8, "c2" -> 7, "d2" -> 6,  "e2" -> 5,  "f2" -> 4,  "g2"-> 3,  "ylatila1"-> 2, "ylatila2" -> 1, "ylatila3" -> 0)

  def kuva: Buffer[String]   
  def pituus : Double
  def soiva: Boolean    // tarvitaan tieto sanoja laittaessa, tauko= false
  
  def piirraTyhjaViivasto(pituus: Int) = {
    
    var viiva = ""
    for ( i <- 1 to pituus) viiva += "-"  // muodostetaan oikean mittainen viiva
    var vali = ""
    for ( i <- 1 to pituus) vali += " "   // muodostetaan oikean mittainen väli
    
    
    for ( i <- 1 to 3)
        viivasto += vali      // ylös tyhjää varsia varten   
    for(i <- 1 to 5){
        viivasto += vali      // ylin paikka on g2 = Map index 3
        viivasto += viiva      // 5 viivaa
    }
    for ( i <- 1 to 4)
        viivasto += vali       // d1, c1, alavali & sanoille tila
        
    viivasto      
  }
 // def pituus : Int  
 // def korkeus: String
  // def aanet: Buffer[Nuotti]   ??  huono design
}
  
 class Sointu(aanet: Buffer[Nuotti]) extends ViivastolleLaitettava{
     def nuotit = aanet   // korkeuden joutuu laskemaan jokaiselle nuotille erikseen
     
   
     def kuva = aanet(0).kuva
     def soiva =  true
     def pituus = aanet(0).pituus   // kaikkien soinnun sävelten tulee olla samanpituisia 
  }
 
 
//trait SoivaSymboli extends ViivastolleLaitettava{    
//}

abstract class Nuotti extends ViivastolleLaitettava {
     def nuppi = "()"
     def ylennysmerkki = "#"
     def alennusmerkki = "b"
     def palautusmerkki = "§"
     
     def soiva = true
  }
 
 abstract class Tauko extends ViivastolleLaitettava{
    def korkeus = "c2"    
    def soiva = false
  }

 
 class KokoNuotti(nuotinNimi: String) extends Nuotti{    
    def korkeus = nuotinNimi
    def pituus = 4.0
    
    def kuva = {
      var viivasto = piirraTyhjaViivasto(10)
      viivasto(y(nuotinNimi)) = viivasto(y(nuotinNimi)).substring(10-2) + nuppi + viivasto(y(nuotinNimi)).substring(4)  
      viivasto
    }
 }   
 

  class PuoliNuotti(nuotinNimi: String) extends KokoNuotti(nuotinNimi: String){
    override def korkeus = nuotinNimi
    override def pituus = 2.0
    
    override def kuva = {
     piirraTyhjaViivasto(8)
     viivasto(y(nuotinNimi)) = viivasto(y(nuotinNimi)).substring(8-2) + nuppi + viivasto(y(nuotinNimi)).substring(4) 
     piirraVarsi
     viivasto
    }   
    
    def piirraVarsi = {
     if(y(nuotinNimi) >= y("h1")){  // varsi ylös  
      for (i <- 1 to 3)
         viivasto(y(nuotinNimi)-i) = viivasto(y(nuotinNimi)-i).substring(8-3) + "|" + viivasto(y(nuotinNimi)-i).substring(4)  
     }  
     else {  // varsi alas 
       for (i <- 1 to 3)
       viivasto(y(nuotinNimi)+i) = viivasto(y(nuotinNimi)+i).substring(8-2) + "|" + viivasto(y(nuotinNimi)+i).substring(3)  
     }
   }  
 }
  

   class PisteellinenPuoliNuotti(nuotinNimi: String) extends PuoliNuotti(nuotinNimi: String){
      override def pituus = 3.0
      
      override def kuva = {
        piirraTyhjaViivasto(8)
        viivasto(y(nuotinNimi)) = viivasto(y(nuotinNimi)).substring(8-2) + nuppi +"." + viivasto(y(nuotinNimi)).substring(5) 
        piirraVarsi
        viivasto
      }  
      
   }
   
    /*
  class NeljasosaNuotti(nuotinNimi: String)  extends PuoliNuotti(nuotit: Map[String, Int], nuotinNimi: String, viivasto: Buffer[Array[Char]], x: Int){ 
       viivasto(nuotit(nuotinNimi))(x) = '@'     // piirtyvät perittyjen () päälle
       viivasto(nuotit(nuotinNimi))(x+1) = '@' 
       
  }
  
   class KahdeksasosaNuotti(nuotinNimi: String)  extends NeljasosaNuotti(nuotit: Map[String, Int], nuotinNimi: String, viivasto: Buffer[Array[Char]], x: Int){
       if(nuotit(nuotinNimi) < nuotit("h1"))      
          viivasto(nuotit(nuotinNimi)+3)(x) = '/'    // väkä 
       else   
          viivasto(nuotit(nuotinNimi)-3)(x+2) = '\\'
       
  }
  
  def piirraKahdeksasosaPari(nuotinNimi: String, toisenNuotinNimi: String) = {
    val toisenNuotinNimiTutk = tutkiEtumerkit(toisenNuotinNimi, x+5) 
    viivasto(nuotitYAkselilla(nuotinNimi))(x)='@'          // 1/8-nuottipari, varret ylös
    viivasto(nuotitYAkselilla(toisenNuotinNimiTutk))(x+5)='@'     
    if(nuotinNimi == "c1"){
       viivasto(nuotitYAkselilla(nuotinNimi))(x-1) = '-'    // keski-c:lle apuviiva
       viivasto(nuotitYAkselilla(nuotinNimi))(x+1) = '-'
    }
    if(toisenNuotinNimi == "c1"){
       viivasto(nuotitYAkselilla(toisenNuotinNimi))(x+4) = '-'    // keski-c:lle apuviiva
       viivasto(nuotitYAkselilla(toisenNuotinNimi))(x+6) = '-'
    }
    val korkeusero = nuotitYAkselilla(nuotinNimi) - nuotitYAkselilla(toisenNuotinNimiTutk)
    println(korkeusero)
    if(korkeusero >= 0 && nuotitYAkselilla(nuotinNimi) > nuotitYAkselilla("h1") || korkeusero < 0 && nuotitYAkselilla(nuotinNimi) < nuotitYAkselilla("h1")){   // parin eka nuotti on alempana, toinen korkeammalla
        piirraVarsi(nuotinNimi, x, korkeusero)
        piirraVarsi(toisenNuotinNimiTutk, x+5, 0)       
        piirraPalkki(nuotitYAkselilla(nuotinNimi) < nuotitYAkselilla("h1"), nuotitYAkselilla(toisenNuotinNimiTutk))  // palkki 2. nuotin korkeudelle
     
    }     
    else  {          // parin eka nuotti on korkeampi, toinen matalampi
       piirraVarsi(nuotinNimi, x, 0)
       piirraVarsi(toisenNuotinNimiTutk, x+5, Math.abs(korkeusero))
       piirraPalkki(nuotitYAkselilla(nuotinNimi) < nuotitYAkselilla("h1"), nuotitYAkselilla(nuotinNimi))    // palkki 1. nuotin korkeudelle
    }   
   }
  
  
  
  
  class NeljasosaTauko extends Tauko { 
      ='\\'
     = '/'
     ='\\'
      = '/'    
       
  }
  
   class KahdeksasosaTauko extends NeljasosaTauko{
      
          = '/'             // väkä        
          = '\\'
       
  }
  
 
  
  
 */