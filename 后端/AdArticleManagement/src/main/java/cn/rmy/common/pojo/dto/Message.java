package cn.rmy.common.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Message {

    @TableId(type = IdType.AUTO)
    private int id;

    //标题
    private String title;

    //日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
