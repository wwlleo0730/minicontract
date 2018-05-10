package cn.iclass.udap.minicontract;

import cn.iclass.udap.minicontract.smartContract.MiniContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;

public class Test {
    public static Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io/aTFmVjYLRjSaUoS2xGeD"));
    public static Credentials credentials = Credentials.create("79972a6328ab7fd6bc90cae4b72fa41e9ad6ee376f39c0ebd5c4926157f1e97f");
    public static MiniContract miniContract = MiniContract.load("7d6fe5e2ad3f4b980f56727a9651f1b0efcfd856",web3j,credentials, BigInteger.valueOf(14000000000l), BigInteger.valueOf(7000000l));
    public static void main(String[] args) throws Exception {
        String txHash = mint("lycrus","wwl","15757813354","who knows","title","content","sound","picture");

        long dbindex = checkMinded(txHash);

        return;
    }

    public static String mint(String A, String B, String Aphone, String Bphone, String tilte, String content, String sound, String picture) throws ExecutionException, InterruptedException {

        TransactionReceipt transactionReceipt =
                miniContract.mint(
                        StringToBytesInUTF8(A),
                        StringToBytesInUTF8(B),
                        StringToBytesInUTF8(Aphone),
                        StringToBytesInUTF8(Bphone),
                        StringToBytesInUTF8(tilte),
                        StringToBytesInUTF8(content),
                        StringToBytesInUTF8(sound),
                        StringToBytesInUTF8(picture)
                ).sendAsync().get();
        return transactionReceipt.getTransactionHash();
    }

    public static long checkMinded(String transactionHash) throws Exception {
        TransactionReceipt transactionReceipt =web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().orElse(null);
        if(transactionReceipt == null){
            throw new Exception("transactionHash isn't found");
        }

        return  miniContract.getEMintEvents(transactionReceipt).get(0).dbindex.longValue();
    }

    public static byte[] StringToBytesInUTF8(String str){
        return str.getBytes(Charset.forName("UTF-8"));
    }

    public static String BytesInUTF8ToString(byte[] b){
        return new String(b,Charset.forName("UTF-8"));
    }
}
