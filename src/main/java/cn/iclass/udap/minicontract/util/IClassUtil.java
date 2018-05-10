package cn.iclass.udap.minicontract.util;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class IClassUtil {

    public byte[] StringToBytesInUTF8(String str){
        return str.getBytes(Charset.forName("UTF-8"));
    }

    public String BytesInUTF8ToString(byte[] b){
        return new String(b,Charset.forName("UTF-8"));
    }
}
