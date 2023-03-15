package cn.rmy.otherFunc;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UpLoad {

    public static int upLoadFile(String resource, MultipartFile file) throws IOException{
        File newFile = new File(resource,"registUsers.xlsx");
        try{
            file.transferTo(newFile);
            System.out.println(newFile.getPath());
            return 0;
        }catch (IOException e){
            e.printStackTrace();
            return -1;
        }
    }
}
