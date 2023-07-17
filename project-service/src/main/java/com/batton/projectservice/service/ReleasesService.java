package com.batton.projectservice.service;

import com.batton.projectservice.common.BaseException;
import com.batton.projectservice.domain.Releases;
import com.batton.projectservice.repository.IssueRepository;
import com.batton.projectservice.repository.ReleasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleasesService {
    private final ReleasesRepository releasesRepository;
}
