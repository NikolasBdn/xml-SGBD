import java.security.*;
import javax.xml.crypto.*;

class SimpleKeySelectorResult implements KeySelectorResult {
       private PublicKey pk;
       SimpleKeySelectorResult(PublicKey pk) {
           this.pk = pk;
       }

       public Key getKey() { return pk; }
   }

