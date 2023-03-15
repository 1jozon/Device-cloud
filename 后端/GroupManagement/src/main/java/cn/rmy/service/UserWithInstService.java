package cn.rmy.service;

import cn.rmy.common.beans.Instrument;
import cn.rmy.common.beans.groupManager.UserT;

import java.util.List;

public interface UserWithInstService {
    List<UserT> getUsersByInstId(String instrumentId);
    List<Instrument> getInstsByUserId(String userId);
}
