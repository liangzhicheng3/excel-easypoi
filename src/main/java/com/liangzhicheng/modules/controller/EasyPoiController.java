package com.liangzhicheng.modules.controller;

import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import com.liangzhicheng.common.basic.ResponseResult;
import com.liangzhicheng.common.utils.LocalJSONUtil;
import com.liangzhicheng.modules.entity.Order;
import com.liangzhicheng.modules.entity.Product;
import com.liangzhicheng.modules.handler.MemberExcelDataHandler;
import com.liangzhicheng.modules.entity.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Api(tags = {"EasyPoi导入导出"})
@RestController
@RequestMapping(value = "/easypoi")
public class EasyPoiController {

    @ApiOperation(value = "导入会员")
    @PostMapping(value = "/import/member")
    @ResponseBody
    public ResponseResult importMember(@RequestPart("file") MultipartFile file){
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        try{
            List<Member> list = ExcelImportUtil.importExcel(
                    file.getInputStream(),
                    Member.class,
                    params);
            // TODO 可进行保存操作
            return ResponseResult.success(list);
        }catch (Exception e){
            log.error("[导入失败] 异常信息:{}", e.getMessage());
            return ResponseResult.failed("导入失败！");
        }
    }

    @ApiOperation(value = "导出会员")
    @GetMapping(value = "/export/member")
    public void exportMember(ModelMap map,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        List<Member> memberList = LocalJSONUtil.jsonToList("json/members.json", Member.class);
        ExportParams params = new ExportParams("会员列表", "会员列表", ExcelType.XSSF);
        //对导出结果进行自定义处理
        MemberExcelDataHandler handler = new MemberExcelDataHandler();
        handler.setNeedHandlerFields(new String[]{"昵称"});
        params.setDataHandler(handler);
        map.put(NormalExcelConstants.DATA_LIST, memberList);
        map.put(NormalExcelConstants.CLASS, Member.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "会员信息");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    @ApiOperation(value = "导出订单")
    @GetMapping(value = "/export/order")
    public void exportOrder(ModelMap map,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        ExportParams params = new ExportParams("订单信息", "订单信息", ExcelType.XSSF);
        //导出时排除字段
        params.setExclusions(new String[]{"订单id", "会员id", "性别", "出生时间", "商品id"});
        map.put(NormalExcelConstants.DATA_LIST, this.listOrder());
        map.put(NormalExcelConstants.CLASS, Order.class);
        map.put(NormalExcelConstants.PARAMS, params);
        map.put(NormalExcelConstants.FILE_NAME, "订单信息");
        PoiBaseView.render(map, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
    }

    private List<Order> listOrder(){
        List<Order> orderList = LocalJSONUtil.jsonToList("json/orders.json", Order.class);
        List<Product> productList = LocalJSONUtil.jsonToList("json/products.json", Product.class);
        List<Member> memberList = LocalJSONUtil.jsonToList("json/members.json", Member.class);
        for(int i = 0; i < orderList.size(); i++){
            Order order = orderList.get(i);
            order.setMember(memberList.get(i));
            order.setProductList(productList);
        }
        return orderList;
    }

}
