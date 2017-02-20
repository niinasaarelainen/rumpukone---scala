import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer
import javax.sound.midi.MidiChannel

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer


class simpleMIDIPlayer (nuotit: Buffer[(Buffer[Int], Int)]) {   // Tuple (korkeus/korkeudet, pituus)
  
  val synth = MidiSystem.getSynthesizer()
  
    val channels  =  synth.getChannels()
		val channel = channels(0)
  
		synth.open()
		Thread.sleep(300)   // jos ei tätä, eka nuotti tulee liian pitkänä, kun synalla/MIDISysteemillä käynnistymiskankeutta
  
    for(nuottiTaiSointu <- nuotit){
      
        for (soinnunNuotti <- nuottiTaiSointu._1)
           channel.noteOn(soinnunNuotti, 115)         // 115 = velocity (127 = max)
           
        Thread.sleep(nuottiTaiSointu._2 * 300)  // ms   
        
        for (soinnunNuotti <- nuottiTaiSointu._1)              
           channel.noteOff(soinnunNuotti)
    }
    Thread.sleep(300)   // parempi soundi vikaan ääneen
    synth.close()
}

class simpleMIDIPlayerAdapter (nuottiData: Buffer[ViivastolleLaitettava]) {   //  used to be: extends App
  
   val MIDINoteNumber = Map("c1" -> 60, "c#1" ->61, "db1" -> 61, "d1" -> 62, "d#1" -> 63, "eb1" -> 63,  "e1" -> 64,  
       "f1"-> 65,  "f#1"->66,  "gb1" -> 66, "g1" -> 67,  "g#1" -> 68, "ab1" -> 68, "a1" -> 69,  
       "a#1" -> 70, "hb1" -> 70, "b1" -> 70, "h1" -> 71,  "c2" -> 72, "c#2" -> 73, "db2" -> 73, "d2" -> 74, 
       "d#2" -> 75, "eb2" -> 75, "e2" -> 76, "f2" -> 77, "f#2" -> 78, "gb2" -> 78, "g2" -> 79, "z" -> 0)

   var nuottiNumberit = Buffer[Buffer[Int]]() 
   var apubufferInt = Buffer[Int]()
   var pituudet = Buffer[Double]()
   
   
   println(nuottiData)
   
       for (alkio<- nuottiData) {
         var apubufferInt = Buffer[Int]()   // luodaan aina tyhjä buffer
         
          alkio.isInstanceOf[Sointu] match {
           case true  => pituudet += alkio.asInstanceOf[Sointu].pituus            // yhteinen pituus talteen vain kerran
                         for(nuotti <- alkio.asInstanceOf[Sointu].nuotit){
                              apubufferInt += MIDINoteNumber(nuotti.asInstanceOf[Nuotti].korkeus)  // Map("nuotinnimi" --> Int)     
                         }   
                         nuottiNumberit += apubufferInt
           case false => if (alkio.isInstanceOf[Nuotti]){
                 apubufferInt += MIDINoteNumber(alkio.asInstanceOf[Nuotti].korkeus)
                 nuottiNumberit += apubufferInt
                 pituudet += alkio.asInstanceOf[Nuotti].pituus
           }     else if (alkio.isInstanceOf[Tauko]) pituudet += alkio.asInstanceOf[Tauko].pituus   // Double
         } 
       }
   
   
   println(nuottiNumberit)
   

    val pituudetInt = pituudet.map { x =>  (x*2).toInt }   // helpompi kertoa millisekunneilla kun kahdeksasosa on 1, ja muut sen moninkertoja
    val nuotitJaPituudet = nuottiNumberit.zip(pituudetInt)
    val player = new simpleMIDIPlayer(nuotitJaPituudet) 
  
}