package kr.co.broadwave.aci.files;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
@Repository
public class FileUploadRepositoryCustomImpl extends QuerydslRepositorySupport implements FileUploadRepositoryCustom {
    public FileUploadRepositoryCustomImpl() {
        super(FileUpload.class);
    }

    @Override
    public List<FileUploadDto> findTop10ByALL() {

        QFileUpload qFileUpload = QFileUpload.fileUpload;

        JPQLQuery<FileUploadDto> query = from(qFileUpload)
                .select(Projections.constructor(FileUploadDto.class,
                        qFileUpload.id,
                        qFileUpload.fileName,
                        qFileUpload.saveFileName,
                        qFileUpload.filePath,
                        qFileUpload.fileFullPath,
                        qFileUpload.contentType,
                        qFileUpload.size

                ));

        return query.limit(10).fetch();

    }
}
