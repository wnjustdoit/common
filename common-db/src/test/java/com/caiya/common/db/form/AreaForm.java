package com.caiya.common.db.form;

import com.caiya.common.db.core.annotation.SQLField;
import com.caiya.common.db.core.annotation.TableName;
import com.caiya.common.db.object.OperationType;
import com.caiya.common.db.object.Page;
import lombok.*;

/**
 * AreaForm.
 *
 * @author wangnan
 * @since 1.0.0, 2020/7/20
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(name = "aaa_copy")
public class AreaForm extends Page {

    private static final long serialVersionUID = 1L;

    @SQLField(primary = true)
    private Long id;

    @SQLField(name = "area_name")
    private String areaName;

    @SQLField(name = "city_id", exclude = {OperationType.UPDATE})
    private Long cityId;

    private Integer isCustom;

    private String others;

}
