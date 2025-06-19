package back.vybz.paymentservice.common.dto.response;

import back.vybz.paymentservice.common.dto.request.RequestPageDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@NoArgsConstructor
@ToString
public class ResponsePageDTO<E> {

    private String type = "USE";

    private List<E> dtoList;

    private List<Integer> pageNumList;

    private RequestPageDTO requestPageDTO;

    private boolean prev, next;

    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder
    public ResponsePageDTO(String type, List<E> dtoList, RequestPageDTO requestPageDTO, long totalCount) {
        this.type = type;
        this.dtoList = dtoList;
        this.requestPageDTO = requestPageDTO;
        this.totalCount = (int)totalCount;

        int currentPage = requestPageDTO.getPage();
        int pageSize = requestPageDTO.getSize();
        int last = (int) Math.ceil(totalCount / (double) pageSize);

        int end = (int)(Math.ceil( requestPageDTO.getPage() / 10.0 )) * 10;
        int start = end - 9;
        end = Math.min(end, last);

        this.prev = currentPage > 1;
        this.next = currentPage < last;

        this.pageNumList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
        this.prevPage = this.prev ? currentPage - 1 : 0;
        this.nextPage = this.next ? currentPage + 1 : 0;
        this.totalPage = this.pageNumList.size();
        this.current = requestPageDTO.getPage();
    }
}
