package org.example.junmin.es.repository;

import org.example.junmin.es.document.BlogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends ElasticsearchRepository<BlogDocument,Long> {
    List<BlogDocument> findByTitleOrContent(
            String title,
            String content
    );
}
