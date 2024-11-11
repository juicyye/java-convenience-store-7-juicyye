package store.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ReaderTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("promotions.md 파일을 읽을 수 있다")
    void readPromotion() throws Exception {
        // given
        Path tempFile = tempDir.resolve("promotionTest.md");
        Files.write(tempFile, List.of(
                "name,buy,get,start_date,end_date"
                , "탄산2+1,2,1,2024-01-01,2024-12-31"
                , "MD추천상품,1,1,2024-01-01,2024-12-31"
                , "반짝할인,1,1,2024-11-01,2024-11-30"
        ));

        // when
        List<String> result = Reader.readFiles(tempFile.toString());

        // then
        assertThat(result).containsExactlyInAnyOrder(
                "탄산2+1,2,1,2024-01-01,2024-12-31"
                , "MD추천상품,1,1,2024-01-01,2024-12-31"
                , "반짝할인,1,1,2024-11-01,2024-11-30");
    }

    @Test
    @DisplayName("product.md 파일을 읽을 수 있다")
    void readProduct() throws Exception {
        // given
        Path tempFile = tempDir.resolve("productTest.md");
        Files.write(tempFile, List.of(
                "name,price,quantity,promotion"
                , "콜라,1000,10,탄산2+1"
                , "콜라,1000,10,null"
                , "사이다,1000,8,탄산2+1"
                , "사이다,1000,7,null"
                , "오렌지주스,1800,9,MD추천상품"
                , "탄산수,1200,5,탄산2+1"
        ));

        // when
        List<String> result = Reader.readFiles(tempFile.toString());

        // then
        assertThat(result).containsExactlyInAnyOrder("콜라,1000,10,탄산2+1"
                , "콜라,1000,10,null"
                , "사이다,1000,8,탄산2+1"
                , "사이다,1000,7,null"
                , "오렌지주스,1800,9,MD추천상품"
                , "탄산수,1200,5,탄산2+1");
    }

}