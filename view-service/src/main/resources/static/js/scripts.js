 /* const 객체 선언 */
// 템플릿 유형 텍스트와 실제 Value를 매핑하는 객체
const smsTypeMap = {
    "정보성 문자": "INFORMAITONAL",
    "광고성 문자": "ADVERTISING",
    "인증 문자": "VERIFICATION"
    // 테이블에 표시되는 모든 유형을 여기에 추가해야 합니다.
};
/* 끝 */

var main = {
    init : function () {

        /* 초기화 섹션 */
        $('#div-choiceCust').hide();
        /* 끝 */

        var _this = this;

        $('#btn-send').on('click', _this.sendSms);

        $('#phoneNumber').on('input', function() {
            $(this).val($(this).val().replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/\-{1,2}$/g, ""));
        });

        $('.cancel-booking').on('click', function () {
            _this.cancelBooking(this);
        });

        $('#placeholdersTable tbody').on('dblclick','tr', function () {
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

        $('.btn-template-edit').on("click", function() {
            _this.editTemplate(this);
        });

        $('#btn-template-cancel-edit').on("click", _this.cancelTemplateEdit);

        $('.btn-variable-edit').on("click", function() {
            _this.editVariable(this);
        });

        $('#btn-variable-cancel-edit').on("click", _this.cancelVariableEdit);

    },

    add : function () {
        var phonenumber = $('#ipt-phonenumber').val();
        if (oper.isEmpty(phonenumber)) {
            alert("전화번호를 입력해주세요.")

        } else {
            var insertTr = "";
            insertTr += "<tr><td>";
            insertTr += phonenumber;
            insertTr += "</td></tr>";
            $('tbody').append(insertTr);
            $('#ipt-phonenumber').val("");
        }

    },

    sendSms : function () {
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

        var itemId = $("#selectItem").val();
        console.log(itemId);

        var data = {
                custIdList: custIdList,
                templateId: template.id,
                sendDt : oper.getTodayDt(),
                itemId : itemId
                }
        oper.ajax("POST",data, '/api/sms/send', callback.sendSms);
    },

    cancelBooking : function (btn) {
        let bookingRow = $(btn).closest("tr");
        console.log(bookingRow);
        var bookingId = bookingRow.find("td").eq(0).text().trim();
        console.log(bookingId)
        var data = {}
        oper.ajax("POST",data,'/api/bookings/'+bookingId+'/cancel', callback.cancelBooking);
    },

    insertPlaceholder : function (tr) {
        var koText = $(tr).find("td").eq(2).text().trim();
        var curr = $('#templateContent').val();
        $('#templateContent').val(curr+"#{"+koText+"} ");
    },

    searchbooking : function () {
        var itemId = $("#selectItem").val();

        if(itemId){
             var data = {
                        'itemId' : itemId ,
                        'bookingStatus' : 'BOOK'
                       };
            oper.ajax("GET", data, "/api/bookings/search", callback.searchbooking);

        } else {
            $("#custBody").empty();
        }
    },

    editTemplate : function (btn) {
        // 1. 수정할 데이터 가져오기
        console.log("row읽기..");
        let selectedRow = $(btn).closest("tr");
        console.log(selectedRow);

        let templateContent = selectedRow.find("td").eq(1).text().trim();
        let smsTypeText = selectedRow.find("td").eq(2).text().trim();

        console.log(templateContent);
        console.log(smsTypeText);

        // 3. 텍스트를 실제 Value로 변환
        const smsTypeValue = smsTypeMap[smsTypeText];

        // 2. 입력 필드에 값 세팅
        $('#templateContent').val(templateContent);
        $('#smsType').val(smsTypeValue);

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#template-register-group').addClass('d-none');
        $('#template-edit-group').removeClass('d-none');
    },

    cancelTemplateEdit : function () {
        console.log("취소할게요");

        // 필드 값 초기화
        $('#templateContent').val('');
        $('#smsType').val('');

        // 기존 버튼 숨기기 및 신규 버튼 추가
        $('#template-register-group').removeClass('d-none');
        $('#template-edit-group').addClass('d-none');
    },

    editVariable : function (btn) {
        // 1. 수정할 데이터 가져오기
        console.log("row읽기..");
        let selectedRow = $(btn).closest("tr");
        console.log(selectedRow);

        let type = selectedRow.find("td").eq(0).text().trim();
        let koText = selectedRow.find("td").eq(1).text().trim();
        let enText = selectedRow.find("td").eq(2).text().trim();

        console.log(type);
        console.log(koText);

        // 2. 입력 필드에 값 세팅
        $('#variableType').val(type);
        $('#koText').val(koText);
        $('#enText').val(enText);

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#variable-register-group').addClass('d-none');
        $('#variable-edit-group').removeClass('d-none');
    },

    cancelVariableEdit : function () {
        console.log("취소할게요");

        // 필드 값 초기화
        $('#variableType').val("");
        $('#koText').val("");
        $('#enText').val("");

        // 3. 기존 버튼 숨기기 및 신규 버튼 추가
        $('#variable-register-group').removeClass('d-none');
        $('#variable-edit-group').addClass('d-none');
    }
};

var callback = {

    choiceCust : function (data) {
        console.log("고객 조회 완료");
        var tbody = $('#tbody');
        tbody.empty();
        data.forEach(cust => {
            var row = '<tr>'
            row+='<td><input type="checkbox" name="chb_cust" value="" </td>'
            row+='<td>'+cust.id+'</td>'
            row+='<td>'+cust.name+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    } ,

    sendSms : function () {
        alert("sms 발송이 완료되었습니다");
        window.location.href='/sms/send';
    } ,

    cancelBooking : function () {
        alert("예매가 취소 되었습니다");
        window.location.href='/bookings';
    } ,

    searchbooking : function (data) {
        var tbody = $('#custBody');
        tbody.empty();
        data.forEach(cust => {
            var row = '<tr>'
            row+='<td><input type="checkbox" class="cust-checkbox"> </td>'
            row+='<td>'+cust.custId+'</td>'
            row+='<td>'+cust.custName+'</td>'
            row+='</tr>'
            tbody.append(row);
        });
    }

}

var oper = {
    isEmpty : function (value) {
        if(value == "" || value == null || value == undefined) {
            return true;
        } else {
            return false;
        }
    },

    ajax : function (type, data, url, callback) {
        // GET이면 쿼리스트링으로 붙이고 data는 제거
        if (type === "GET" && data && Object.keys(data).length > 0) {
            const queryString = $.param(data); // itemId=123&bookingStatus=BOOK
            url += (url.includes("?") ? "&" : "?") + queryString;
            data = null;
        }

        $.ajax({
            'type': type,
            'url':url,
            'dataType':'json',
            'contentType':'application/json; charset=utf-8',
            'data': type === "GET" ? null : JSON.stringify(data)
        })
        .done(function (response){
            callback(response);
        })
        .fail(function(xhr, status, error) {
            // 요청이 실패했을 때 실행되는 코드
            console.error('요청 실패:', xhr.responseJSON);
            var errMsg = xhr.responseJSON.message;
            alert(errMsg);
        })
//        .always(function (){
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
    }
}

main.init();