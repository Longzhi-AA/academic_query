package org.acq.lz.service;

import org.acq.lz.service.query.CommonSearchResult;

import java.util.List;

public interface SearchEngin {

    List<CommonSearchResult> search(String query);
}
