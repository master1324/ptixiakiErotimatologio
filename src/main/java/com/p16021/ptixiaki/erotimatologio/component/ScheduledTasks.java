package com.p16021.ptixiaki.erotimatologio.component;

import com.p16021.ptixiaki.erotimatologio.repos.FilterRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

//@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledTasks {

    private final FilterRepo filterRepo;

    //@Scheduled(fixedRate = 30000)
    //@Transactional
    public void reportCurrentTime() {
        //log.info("Checking active filters");
        filterRepo.disableFilters(System.currentTimeMillis());
        //log.info("The time is now {}", dateFormat.format(new Date()));
    }

}
