package cn.autumn.wishbackstage.service.impl;

import cn.autumn.wishbackstage.config.database.GenerateTableConfig;
import cn.autumn.wishbackstage.mapper.DatabaseMapper;
import cn.autumn.wishbackstage.service.DatabaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cf
 * Created in 2022/11/2
 */
@Service
public final class DatabaseServiceImpl implements DatabaseService {

    @Resource
    private GenerateTableConfig generateTableConfig;

    @Resource
    private DatabaseMapper databaseMapper;

    @Override
    public List<String> getTableList() {
        return databaseMapper.getTableList(generateTableConfig.getDatabaseByUrl());
    }

}
