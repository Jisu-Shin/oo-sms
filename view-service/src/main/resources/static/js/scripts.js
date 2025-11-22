 /* const 객체 선언 */
// 템플릿 유형 텍스트와 실제 Value를 매핑하는 객체
const smsTypeMap = {
    "정보성 문자": "INFORMAITONAL",
    "광고성 문자": "ADVERTISING",
    "인증 문자": "VERIFICATION"
    // 테이블에 표시되는 모든 유형을 여기에 추가해야 합니다.
};
/* 끝 */

/* 전역 변수 선언 */
var selectedTemplateRow = null;
var selectedVariableRow = null;
/* 끝 */

var main = {
    init : function() {

        var _this = this;

        _this.setDefaultDates();

        $('#btn-send').on('click', _this.sendSms);

        $('#phoneNumber').on('input', function() {
            $(this).val($(this).val().replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/\-{1,2}$/g, ""));
        });

        $('.cancel-booking').on('click', function() {
            _this.cancelBooking(this);
        });

        $('#placeholdersTable tbody').on('dblclick','tr', function() {
            _this.insertPlaceholder(this);
        });

        $('#selectItem').on('change', _this.searchbooking);

        $('#price').on("input", function() {
            let price = $(this).val();

            // 1. 쉼표나 문자가 아닌 모든 것 제거 (순수 숫자 문자열만 남김)
            let cleaned = price.replace(/[^0-9]/g, "");

            // 2. 숫자가 있다면
            if (cleaned) {
                // 3. 숫자로 변환 후 toLocaleString()으로 쉼표 추가
                let formatted = Number(cleaned).toLocaleString('ko-KR');

                // 4. 입력 필드에 적용
                $(this).val(formatted);
            } else {
                // 5. 비어있다면 값도 비움
                $(this).val('');
            }
        });

        /* sms템플릿, 템플릿 변수 수정 삭제 버튼 */
        $('.btn-template-edit').on("click", function() {
            _this.editTemplate(this);
        });

        $('#btn-template-cancel-edit').on("click", _this.cancelTemplateEdit);

        $('#btn-template-delete').on("click", _this.deleteTemplate);

        $('#btn-template-confirm-edit').on("click", _this.confirmEditTemplate);

        $('.btn-variable-edit').on("click", function() {
            _this.editVariable(this);
        });

        $('#btn-variable-cancel-edit').on("click", _this.cancelVariableEdit);

        $('#btn-variable-delete').on("click", _this.deleteVariable);

        $('#btn-variable-confirm-edit').on("click", _this.confirmEditVariable);
        /* 끝 */

        $('#btn-createCust').on("click", _this.createCust);

        $('#btn-createBooking').on("click", _this.createBooking);

        $('#btn-template-save').on("click", _this.createSmsTemplate);

        $('#btn-variable-save').on("click", _this.createTemplateVariable);

        $('#btn-createItem').on("click", _this.createItem);

    },

    setDefaultDates: function() {
//        let startDateString = oper.getSevenDaysAgo().substr(0,8);
        let startDateString = oper.getSevenDaysAgo().replace(/^(\d{4})(\d{2})(\d{2})(\d{4})$/, "$1-$2-$3");
        let endDateString = oper.getTodayDt().replace(/^(\d{4})(\d{2})(\d{2})(\d{4})$/, "$1-$2-$3");

        console.log(startDateString);
        console.log(endDateString);

        if (!$('#endDate').val()) {
            $('#endDate').val(endDateString);
        }

        if (!$('#startDate').val()) {
            $('#startDate').val(startDateString);
        }
    } ,

    createItem: function() {
        const form = $('#itemForm');
        const formDataArray = form.serializeArray();
        const data = {};
        $.each(formDataArray, function() {
          data[this.name] = this.value;
        });

        console.log(data);

        oper.ajax("POST", data, "/api/items", callback.createItem);
    } ,

    createTemplateVariable: function() {
        const form = $('#templateVariableForm');
        const formDataArray = form.serializeArray();
        const data = {};
        $.each(formDataArray, function() {
          data[this.name] = this.value;
        });

        console.log(data);

        oper.ajax("POST", data, "/api/templateVariables", callback.createTemplateVariable);
    },

    createSmsTemplate : function() {
        const form = $('#smsTemplateForm');
        const formDataArray = form.serializeArray();
        const data = {};
        $.each(formDataArray, function() {
          data[this.name] = this.value;
        });

        console.log(data);

        oper.ajax("POST", data, "/api/smsTemplates", callback.createSmsTemplate);
    },

    createBooking : function() {
        const form = $('#bookingForm');
        const formDataArray = form.serializeArray();
        const data = {};
        $.each(formDataArray, function() {
          data[this.name] = this.value;
        });

        console.log(data);

        oper.ajax("POST", data, "/api/bookings", callback.createBooking);
    },

    createCust : function() {
        const form = $('#custForm');
        // 1. 폼 데이터를 URL 인코딩 문자열로 직렬화 (jQuery 표준)
        const formDataArray = form.serializeArray();

        // 2. 직렬화된 배열을 일반 JavaScript 객체로 변환
        const data = {};
        $.each(formDataArray, function() {
          data[this.name] = this.value;
        });

        console.log(data);

        oper.ajax("POST", data, "/api/custs", callback.createCust);
    },

    add : function() {
        let phonenumber = $('#ipt-phonenumber').val();
        if (oper.isEmpty(phonenumber)) {
            alert("전화번호를 입력해주세요.")

        } else {
            let insertTr = "";
            insertTr += "<tr><td>";
            insertTr += phonenumber;
            insertTr += "</td></tr>";
            $('tbody').append(insertTr);
            $('#ipt-phonenumber').val("");
        }

    },

    sendSms : function() {
        let custIdList = [];
        $(".cust-checkbox:checked").each(function() {
            let custId = $(this).closest("tr").find("td").eq(1).text().trim();
            custIdList.push(custId);
        });

        let selectedRow = $(".template-radio:checked").closest("tr");

//        if (selectedRow.length === 0) {
//            alert("선택된 행이 없습니다.");
//            return;
//        }

        let template = {
            id: selectedRow.find("td").eq(1).text().trim(),
            smsType: selectedRow.find("td").eq(3).text().trim(),
        };

        console.log(selectedRow);
        console.log(template);
        console.log(oper.getTodayDt());

        let itemId = $("#selectItem").val();
        console.log(itemId);

        let data = {
                custIdList: custIdList,
                templateId: template.id,
                sendDt : oper.getTodayDt(),
                itemId : itemId
                }
        oper.ajax("POST",data, '/api/sms/send', callback.sendSms);
    },

    cancelBooking : function(btn) {
        let bookingRow = $(btn).closest("tr");
        console.log(bookingRow);
        let bookingId = bookingRow.find("td").eq(0).text().trim();
        console.log(bookingId)
        let data = {}
        oper.ajax("POST",data,'/api/bookings/'+bookingId+'/cancel', callback.cancelBooking);
    },

    insertPlaceholder : function(tr) {
        let koText = $(tr).find("td").eq(3).text().trim();
        let curr = $('#templateContent').val();
        $('#templateContent').val(curr+"#{"+koText+"} ");
    },

    searchbooking : function() {
        let itemId = $("#selectItem").val();

        if(itemId){
             let data = {
                        'itemId' : itemId ,
                        'bookingStatus' : 'BOOK'
                       };
            oper.ajax("GET", data, "/api/bookings/search", callback.searchbooking);

        } else {
            $("#custBody").empty();
        }
    },

    editTemplate : function(btn) {
        // 1. 수정할 데이터 가져오기
        console.log("row읽기..");
        if (oper.isEmpty(selectedTemplateRow) == false) {
            selectedTemplateRow.removeClass('table-active');
        }
        selectedTemplateRow = $(btn).closest("tr");
        selectedTemplateRow.addClass('table-active');
        console.log(selectedTemplateRow);

        let templateId = selectedTemplateRow.find("td").eq(0).text().trim();
        let templateContent = selectedTemplateRow.find("td").eq(1).text().trim();
        let smsTypeText = selectedTemplateRow.find("td").eq(2).text().trim();

        console.log(templateContent);
        console.log(smsTypeText);

        // 3. 텍스트를 실제 Value로 변환
        const smsTypeValue = smsTypeMap[smsTypeText];

        // 2. 입력 필드에 값 세팅
        $('#template-id').val(templateId);
        $('#templateContent').val(templateContent);
        $('#smsType').val(smsTypeValue);

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#template-register-group').addClass('d-none');
        $('#template-edit-group').removeClass('d-none');
    },

    cancelTemplateEdit : function() {
        console.log("취소할게요");
        selectedTemplateRow.removeClass('table-active');

        // 필드 값 초기화
        $('#templateContent').val('');
        $('#smsType').val('');

        // 기존 버튼 숨기기 및 신규 버튼 추가
        $('#template-register-group').removeClass('d-none');
        $('#template-edit-group').addClass('d-none');
    },

    editVariable : function(btn) {
        // 1. 수정할 데이터 가져오기
        console.log("row읽기..");
        if (oper.isEmpty(selectedVariableRow) == false) {
            selectedVariableRow.removeClass('table-active');
        }

        selectedVariableRow = $(btn).closest("tr");
        selectedVariableRow.addClass('table-active');
        console.log(selectedVariableRow);

        let id = selectedVariableRow.find("td").eq(0).text().trim();
        let type = selectedVariableRow.find("td").eq(1).text().trim();
        let enText = selectedVariableRow.find("td").eq(2).text().trim();
        let koText = selectedVariableRow.find("td").eq(3).text().trim();

        console.log(type);
        console.log(koText);

        // 2. 입력 필드에 값 세팅
        $('#variable-id').val(id);
        $('#displayVarType').val(type);
        $('#koText').val(koText);
        $('#enText').val(enText);

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#variable-register-group').addClass('d-none');
        $('#variable-edit-group').removeClass('d-none');
    },

    cancelVariableEdit : function() {
        console.log("취소할게요");
        selectedVariableRow.removeClass('table-active');

        // 필드 값 초기화
        $('#variableType').val("");
        $('#koText').val("");
        $('#enText').val("");

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#variable-register-group').removeClass('d-none');
        $('#variable-edit-group').addClass('d-none');
    },

    deleteTemplate : function() {
        let templateId = $('#template-id').val();
        if (confirm("템플릿을 삭제하시겠습니까?") == true) {
            oper.ajax("DELETE", {}, '/api/smsTemplates/'+templateId, callback.deleteTemplate);
        }
    },

    deleteVariable : function() {
        let variableId = $('#variable-id').val();
        if (confirm("템플릿변수를 삭제하시겠습니까?") == true) {
            oper.ajax("DELETE", {}, '/api/templateVariables/'+variableId, callback.deleteVariable);
        }
    },

    confirmEditTemplate : function() {
        console.log("수정수정");
        if (confirm("템플릿을 수정하시겠습니까?") == true) {
            let data = {
                id : $('#template-id').val() ,
                templateContent : $('#templateContent').val() ,
                smsType : $('#smsType').val()
            };
            oper.ajax("POST", data, '/api/smsTemplates/edit', callback.confirmEditTemplate);
        }
    },

    confirmEditVariable : function() {
        console.log("수정수정");
        if (confirm("템플릿변수를 수정하시겠습니까?") == true) {
            let data = {
                id : $('#variable-id').val() ,
                koText : $('#koText').val(),
                enText : $('#enText').val(),
                displayVarType : $('#displayVarType').val()
            };
            oper.ajax("POST", data, '/api/templateVariables/edit', callback.confirmEditVariable);
        }
    }
};

var callback = {

    choiceCust : function(data) {
        console.log("고객 조회 완료");
        let tbody = $('#tbody');
        tbody.empty();
        data.forEach(cust => {
            let row = '<tr>'
            row+='<td><input type="checkbox" name="chb_cust" value="" </td>'
            row+='<td>'+cust.id+'</td>'
            row+='<td>'+cust.name+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    } ,

    sendSms : function() {
        alert("sms 발송이 완료되었습니다");
        window.location.href='/sms/send';
    } ,

    cancelBooking : function() {
        alert("예매가 취소 되었습니다");
        window.location.href='/bookings';
    } ,

    searchbooking : function(data) {
        let tbody = $('#custBody');
        tbody.empty();
        data.forEach(cust => {
            let row = '<tr>'
            row+='<td><input type="checkbox" class="cust-checkbox"> </td>'
            row+='<td>'+cust.custId+'</td>'
            row+='<td>'+cust.custName+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    } ,

    deleteTemplate : function() {
        alert("템플릿이 삭제되었습니다.");
        window.location.href='/smsTemplates/new';
    } ,

    deleteVariable : function() {
        alert("템플릿변수가 삭제되었습니다.");
        window.location.href='/smsTemplates/new';
    } ,

    confirmEditTemplate : function() {
        alert("템플릿이 수정되었습니다.");
        window.location.href='/smsTemplates/new';
    } ,

    confirmEditVariable : function() {
        alert("템플릿변수가 수정되었습니다.");
        window.location.href='/smsTemplates/new';
    },
    
    createCust : function() {
        alert("고객등록이 완료되었습니다.");
        window.location.href='/custs';
    } ,

    createBooking : function() {
        alert("예매가 완료되었습니다.");
        window.location.href="/bookings/new";
    } ,

    createSmsTemplate : function() {
        alert("sms템플릿이 생성되었습니다.");
        window.location.href="/smsTemplates/new";
    } ,

    createTemplateVariable : function() {
        alert("템플릿변수가 생성되었습니다.");
        window.location.href="/smsTemplates/new";
    } ,

    createItem : function() {
        alert("공연이 생성되었습니다.");
        window.location.href="/items";
    }

    /* 콜백끝 */
}

var oper = {
    isEmpty : function(value) {
        if(value == "" || value == null || value == undefined) {
            return true;
        } else {
            return false;
        }
    },

    ajax : function(type, data, url, callback) {
        // GET이면 쿼리스트링으로 붙이고 data는 제거
        if (type === "GET" && data && Object.keys(data).length > 0) {
            const queryString = $.param(data); // itemId=123&bookingStatus=BOOK
            url += (url.includes("?") ? "&" : "?") + queryString;
            data = null;
        }

        $.ajax({
            'type': type,
            'url':url,
//            'dataType':'json',
            'contentType':'application/json; charset=utf-8',
            'data': type === "GET" ? null : JSON.stringify(data)
        })
        .done(function(response){
            callback(response);
        })
        .fail(function(xhr, status, error) {
            // 요청이 실패했을 때 실행되는 코드
            console.error('요청 실패:', xhr);
            let errMsg = xhr.responseJSON?.message || xhr.responseText || error || '알 수 없는 오류';

            // Validation 에러인 경우 필드별 에러도 표시
            if (xhr.responseJSON?.errors) {
                let fieldErrors = xhr.responseJSON.errors;
                let errorDetails = '\n';
                for (let field in fieldErrors) {
                    errorDetails += `- ${fieldErrors[field]}\n`;
                }
                errMsg += errorDetails;
            }

            alert(errMsg);
        })
//        .always(function(){
//            console.log("ajax always 로그");
//        });
    },

    getTodayDt : function() {
        let today = new Date();
        let year = String(today.getFullYear());
        let month = String(today.getMonth()+1).padStart(2,"0");
        let date = String(today.getDate()).padStart(2,"0");
        let hours = String(today.getHours()).padStart(2,"0");
        let minutes = String(today.getMinutes()).padStart(2,"0");
        return year+month+date+hours+minutes;
    },

    getSevenDaysAgo : function() {
        let today = new Date();

        let sevenDaysAgo = new Date();
        sevenDaysAgo.setDate(today.getDate() - 7);

        let year = String(sevenDaysAgo.getFullYear());
        let month = String(sevenDaysAgo.getMonth()+1).padStart(2,"0");
        let date = String(sevenDaysAgo.getDate()).padStart(2,"0");
        let hours = String(sevenDaysAgo.getHours()).padStart(2,"0");
        let minutes = String(sevenDaysAgo.getMinutes()).padStart(2,"0");

        return year+month+date+hours+minutes;
    }
}

main.init();