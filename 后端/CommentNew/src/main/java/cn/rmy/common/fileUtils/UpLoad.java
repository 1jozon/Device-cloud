package cn.rmy.common.fileUtils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UpLoad {
    public static int upLoadFile(String resource,String fileName,MultipartFile file) throws IOException {

        File newFile = new File(resource,fileName);

        if(!newFile.exists()){
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            file.transferTo(newFile);
            System.out.println(newFile.getPath());
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
