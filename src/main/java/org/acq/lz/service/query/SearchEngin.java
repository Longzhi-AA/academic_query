package org.acq.lz.service.query;

import java.util.List;

public interface SearchEngin {

    List<CommonSearchResult> search(String query);
}
