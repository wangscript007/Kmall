package xyz.klenkiven.kmall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import xyz.klenkiven.kmall.product.entity.BrandEntity;
import xyz.klenkiven.kmall.product.service.BrandService;

import java.io.ByteArrayInputStream;

@SpringBootTest
class KmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    OSS ossClient;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("KlenKiven");
        // brandService.save(brandEntity);
    }

    @Test
    void ossClient() {
        // 填写字符串。
        String content = "Hello OSS";

        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        PutObjectRequest putObjectRequest = new PutObjectRequest("klenkiven-kmall", "exampledir/exampleobject.txt", new ByteArrayInputStream(content.getBytes()));

        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传字符串。
        ossClient.putObject(putObjectRequest);
    }

}
