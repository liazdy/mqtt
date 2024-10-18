package com.gyjs.mqtt.admin.repository;

import com.gyjs.mqtt.admin.repository.base.BaseRepository;
import com.gyjs.mqtt.admin.table.DataCurrent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DataCurrentService extends BaseRepository<DataCurrent, Long>, JpaSpecificationExecutor<DataCurrent> {
    DataCurrent findByClientId(String clientId);
    // DataCurrent findOneById(Long id);
}
