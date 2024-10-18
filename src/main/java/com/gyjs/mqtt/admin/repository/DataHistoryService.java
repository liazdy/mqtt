package com.gyjs.mqtt.admin.repository;

import com.gyjs.mqtt.admin.repository.base.BaseRepository;
import com.gyjs.mqtt.admin.table.DataHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DataHistoryService extends BaseRepository<DataHistory, Long> {
    int countDataHistoriesByClientId(String clientId);

    Page<DataHistory> findAllByClientId(String clientId, Pageable pageable);

    //DataHistory findOneById(Long id);
}
