import crypto.Tuple
import crypto.proxy.afgh.{AFGHProxyReEncryption, AFGHGlobalParameters}
import it.unisa.dia.gas.jpbc.{PairingPreProcessing, ElementPowPreProcessing, Element}
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._

@RunWith(classOf[JUnitRunner])
class MainSpec extends Specification {

  "Proxy Re-encryption" should {

    "be happy" in {
      var cpuTime: Long = 0L
      var time = new Array[Long](20)
      var i: Int = 0

      def medirTiempoMicroSegundos: Long = {
        time(i) = (System.nanoTime - cpuTime) / 1000
        i += 1
        cpuTime = System.nanoTime
        return time(i)
      }

      //java.security.

      cpuTime = System.nanoTime()

      // 80 bits seg: r = 160, q = 512
      // 128 bits seg: r = 256, q = 1536
      // 256 bits seg: r = 512, q = 7680

      val rBits = 256 //160;    // 20 bytes
      val qBits = 1536 //512;    // 64 bytes

      val global = new AFGHGlobalParameters(rBits, qBits);

      medirTiempoMicroSegundos;

      // Secret keys
      val sk_a: Element = AFGHProxyReEncryption.generateSecretKey(global)

      medirTiempoMicroSegundos

      val sk_b: Element = AFGHProxyReEncryption.generateSecretKey(global)

      medirTiempoMicroSegundos

      val sk_b_inverse: Element = sk_b.invert

      medirTiempoMicroSegundos

      // Public keys
      val pk_a: Element = AFGHProxyReEncryption.generatePublicKey(sk_a, global)

      medirTiempoMicroSegundos

      val pk_b: Element = AFGHProxyReEncryption.generatePublicKey(sk_b, global)

      medirTiempoMicroSegundos

      val pk_a_ppp: ElementPowPreProcessing = pk_a.getElementPowPreProcessing

      medirTiempoMicroSegundos

      // Re-Encryption Key
      val rk_a_b: Element = AFGHProxyReEncryption.generateReEncryptionKey(pk_b, sk_a)

      medirTiempoMicroSegundos

      val message: String = "12345678901234567890123456789012"
      val m: Element = AFGHProxyReEncryption.stringToElement(message, global.getG2)

      medirTiempoMicroSegundos

      val c_a: Tuple = AFGHProxyReEncryption.secondLevelEncryption(m, pk_a_ppp, global)

      medirTiempoMicroSegundos

      val e_ppp: PairingPreProcessing = global.getE.getPairingPreProcessingFromElement(rk_a_b)

      medirTiempoMicroSegundos

      val c_b: Tuple = AFGHProxyReEncryption.reEncryption(c_a, rk_a_b, e_ppp)

      medirTiempoMicroSegundos

      val m2: Element = AFGHProxyReEncryption.firstLevelDecryptionPreProcessing(c_b, sk_b_inverse, global)

      medirTiempoMicroSegundos

      {
        var j: Int = 0
        while (j < i) {
          {
            System.out.println(time(j))
          }
          ({
            j += 1;
            j - 1
          })
        }
      }

      message must equalTo(new String(m2.toBytes).trim)

    }

  }
}
