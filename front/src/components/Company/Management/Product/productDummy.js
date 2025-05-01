import dmp1 from "../../../../image/dummydata/weddingdress1.jpg";
import dmp2 from "../../../../image/dummydata/weddingdress2.jpg";
import dmp3 from "../../../../image/dummydata/weddingdress3.jpg";
import dmp4 from "../../../../image/dummydata/weddingdress4.jpg";
import dmp5 from "../../../../image/dummydata/weddingdress5.jpg";
import dmp6 from "../../../../image/dummydata/weddingdress6.jpg";
import dmp7 from "../../../../image/dummydata/weddingdress7.jpg";
import dmp8 from "../../../../image/dummydata/weddingdress8.jpg";
import dmp9 from "../../../../image/dummydata/weddingdress9.jpg";
import dmp10 from "../../../../image/dummydata/weddingdress10.jpg";
import dmp11 from "../../../../image/dummydata/weddingdress11.jpg";


const productDummy = [
    {
        id: "1",
        category: "D",
        name: "드레스 22호 (흰)",
        price: 100000,
        status: "ACTIVE",
        rec: "Y",
        task_time: 120,
        in_available: "Y",
        out_available: "N",
        color: "white",
        image_url_list: [dmp1, dmp2],
        description: `
        <h2>👰 드레스 상세 설명</h2>
        <img src="${dmp1}" alt="드레스 상세 이미지" style="max-width: 100%; border-radius: 10px; margin: 20px 0;" />
        <p>이 드레스는 봄 시즌 한정으로 제작된 고급 레이스 드레스입니다.</p>
        <ul>
            <li>사이즈: S, M, L</li>
            <li>색상: 화이트</li>
            <li>소재: 실크 & 레이스</li>    
        </ul>
        `,
        option_list: [
        {
            name: "사이즈",
            overlap: "N",
            essential: "Y",
            status: "ACTIVE",
            option_dt_list: [
            { op_dt_name: "small", plus_cost: 50000 },
            { op_dt_name: "midium", plus_cost: 50000 },
            { op_dt_name: "large", plus_cost: 50000 },
            { op_dt_name: "x-larg", plus_cost: 100000 }
            ]
        },
        {
            name: "추가 대여",
            overlap: "N",
            essential: "N",
            status: "ACTIVE",
            option_dt_list: [
            { op_dt_name: "한시간 추가 대여", plus_cost: 50000, plus_time: 60 },
            { op_dt_name: "두시간 추가 대여", plus_cost: 100000, plus_time: 120 }
            ]
        }
        ]
    },
    {
        id: "2",
        category: "D",
        name: "드레스 23호 (핑크)",
        price: 120000,
        status: "ACTIVE",
        rec: "N",
        task_time: 90,
        in_available: "Y",
        out_available: "Y",
        color: "pink",
        image_url_list: [dmp4, dmp3],
        description: `
        <h2>🌸 봄날 핑크 드레스</h2>
        <img src="${dmp3}" alt="드레스 이미지" style="max-width: 100%; border-radius: 10px; margin: 20px 0;" />
        <p>화사한 느낌의 핑크 드레스는 실내외 촬영 모두에 적합합니다.</p>
        <ul>
            <li>사이즈: Free</li>
            <li>색상: 핑크</li>
            <li>소재: 쉬폰 & 폴리 혼방</li>    
        </ul>
        `,
        option_list: [
        {
            name: "촬영 옵션",
            overlap: "Y",
            essential: "Y",
            status: "ACTIVE",
            option_dt_list: [
            { op_dt_name: "스냅 추가", plus_cost: 30000, plus_time: 20 },
            { op_dt_name: "배경지 변경", plus_cost: 10000, plus_time: 0 }
            ]
        }
        ]
    },
    {
        id: "3",
        category: "D",
        name: "드레스 24호 (블루)",
        price: 130000,
        status: "ACTIVE",
        rec: "Y",
        task_time: 60,
        in_available: "N",
        out_available: "Y",
        color: "blue",
        image_url_list: [dmp6, dmp5],
        description: `
        <h2>💙 시원한 블루 드레스</h2>
        <img src="${dmp6}" alt="블루 드레스 이미지" style="max-width: 100%; border-radius: 10px; margin: 20px 0;" />
        <p>시원하고 깔끔한 느낌의 블루 드레스는 여름 야외촬영에 적합합니다.</p>
        <ul>
            <li>사이즈: S, M</li>
            <li>색상: 블루</li>
            <li>소재: 면 + 폴리</li>    
        </ul>
        `,
        option_list: [
        {
            name: "헤어소품",
            overlap: "N",
            essential: "N",
            status: "ACTIVE",
            option_dt_list: [
            { op_dt_name: "헤어밴드", plus_cost: 5000, plus_time: 0 },
            { op_dt_name: "왕관", plus_cost: 10000, plus_time: 0 }
            ]
        }
        ]
    },
    {
        id: "4",
        category: "D",
        name: "드레스 25호 (레드)",
        price: 140000,
        status: "INACTIVE",
        rec: "N",
        task_time: 90,
        in_available: "Y",
        out_available: "N",
        color: "red",
        image_url_list: [dmp7],
        description: `<p>열정적인 레드 컬러로 포인트를 주는 드레스입니다.</p>`,
        option_list: []
    },
    {
        id: "5",
        category: "D",
        name: "드레스 26호 (옐로우)",
        price: 150000,
        status: "INACTIVE",
        rec: "N",
        task_time: 60,
        in_available: "N",
        out_available: "Y",
        color: "yellow",
        image_url_list: [dmp8],
        description: `<p>따뜻한 봄 햇살을 닮은 옐로우 드레스입니다.</p>`,
        option_list: []
    },
    {
        id: "6",
        category: "D",
        name: "드레스 27호 (퍼플링)",
        price: 160000,
        status: "INACTIVE",
        rec: "Y",
        task_time: 120,
        in_available: "Y",
        out_available: "Y",
        color: "purple",
        image_url_list: [dmp9],
        description: `<p>우아하고 고급스러운 느낌의 보라빛 드레스입니다.</p>`,
        option_list: []
    },
    {
        id: "7",
        category: "D",
        name: "드레스 22호 (블랙)",
        price: 300000,
        status: "PACKAGE",
        rec: "N",
        task_time: 180,
        in_available: "Y",
        out_available: "Y",
        color: "black",
        image_url_list: [dmp10],
        description: `<p>스냅 + 영상 + 드레스 포함된 패키지 상품입니다.</p>`,
        option_list: []
    },
    {
        id: "8",
        category: "D",
        name: "드레스 22호 (베이지)",
        price: 350000,
        status: "PACKAGE",
        rec: "Y",
        task_time: 180,
        in_available: "Y",
        out_available: "N",
        color: "beige",
        image_url_list: [dmp11],
        description: `<p>드레스 + 메이크업 + 액세서리 포함된 프리미엄 패키지입니다.</p>`,
        option_list: []
    }
];

export default productDummy;
