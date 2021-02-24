layui.define(['layer'], function(exports) {
	"use strict";

	var industry = {
		/**
		 * 组织机构类别选项
		 * @param {String} msg
		 */

		data: function() {
			return [
                {
                    "ids": "a",
                    "names": "农、林、牧、渔业",
                    "childrens": [
                        {
                            "ids": "a01",
                            "names": "农业",
                            "childrens": [
                                {
                                    "ids": "a011",
                                    "names": "谷物种植",
                                    "childrens": [
                                        {
                                            "ids": "a0111",
                                            "names": "稻谷种植"
                                        },
                                        {
                                            "ids": "a0112",
                                            "names": "小麦种植"
                                        },
                                        {
                                            "ids": "a0113",
                                            "names": "玉米种植"
                                        },
                                        {
                                            "ids": "a0119",
                                            "names": "其他谷物种植"
                                        }]
                                },
                                {
                                    "ids": "a012",
                                    "names": "豆类、油料和薯类种植",
                                    "childrens": [
                                        {
                                            "ids": "a0121",
                                            "names": "豆类种植"
                                        },
                                        {
                                            "ids": "a0122",
                                            "names": "油料种植"
                                        },
                                        {
                                            "ids": "a0123",
                                            "names": "薯类种植"
                                        }]
                                },
                                {
                                    "ids": "a013",
                                    "names": "棉、麻、糖、烟草种植",
                                    "childrens": [
                                        {
                                            "ids": "a0131",
                                            "names": "棉花种植"
                                        },
                                        {
                                            "ids": "a0132",
                                            "names": "麻类种植"
                                        },
                                        {
                                            "ids": "a0133",
                                            "names": "糖料种植"
                                        },
                                        {
                                            "ids": "a0134",
                                            "names": "烟草种植"
                                        }]
                                },
                                {
                                    "ids": "a014",
                                    "names": "蔬菜、食用菌及园艺作物种植",
                                    "childrens": [
                                        {
                                            "ids": "a0141",
                                            "names": "蔬菜种植"
                                        },
                                        {
                                            "ids": "a0142",
                                            "names": "食用菌种植"
                                        },
                                        {
                                            "ids": "a0143",
                                            "names": "花卉种植"
                                        },
                                        {
                                            "ids": "a0149",
                                            "names": "其他园艺作物种植"
                                        }]
                                },
                                {
                                    "ids": "a015",
                                    "names": "水果种植",
                                    "childrens": [
                                        {
                                            "ids": "a0151",
                                            "names": "仁果类和核果类水果种植"
                                        },
                                        {
                                            "ids": "a0152",
                                            "names": "葡萄种植"
                                        },
                                        {
                                            "ids": "a0153",
                                            "names": "柑橘类种植"
                                        },
                                        {
                                            "ids": "a0154",
                                            "names": "香蕉等亚热带水果种植"
                                        },
                                        {
                                            "ids": "a0159",
                                            "names": "其他水果种植"
                                        }]
                                },
                                {
                                    "ids": "a016",
                                    "names": "坚果、含油果、香料和饮料作物种植",
                                    "childrens": [
                                        {
                                            "ids": "a0161",
                                            "names": "坚果种植"
                                        },
                                        {
                                            "ids": "a0162",
                                            "names": "含油果种植"
                                        },
                                        {
                                            "ids": "a0163",
                                            "names": "香料作物种植"
                                        },
                                        {
                                            "ids": "a0169",
                                            "names": "茶及其他饮料作物种植"
                                        }]
                                },
                                {
                                    "ids": "a017",
                                    "names": "中药材种植",
                                    "childrens": [
                                        {
                                            "ids": "a0170",
                                            "names": "中药材种植"
                                        }]
                                },
                                {
                                    "ids": "a019",
                                    "names": "其他农业",
                                    "childrens": [
                                        {
                                            "ids": "a0190",
                                            "names": "其他农业"
                                        }]
                                }]
                        },
                        {
                            "ids": "a02",
                            "names": "林业",
                            "childrens": [
                                {
                                    "ids": "a021",
                                    "names": "林木育种和育苗",
                                    "childrens": [
                                        {
                                            "ids": "a0211",
                                            "names": "林木育种"
                                        },
                                        {
                                            "ids": "a0212",
                                            "names": "林木育苗"
                                        }]
                                },
                                {
                                    "ids": "a022",
                                    "names": "造林和更新",
                                    "childrens": [
                                        {
                                            "ids": "a0220",
                                            "names": "造林和更新"
                                        }]
                                },
                                {
                                    "ids": "a023",
                                    "names": "森林经营和管护",
                                    "childrens": [
                                        {
                                            "ids": "a0230",
                                            "names": "森林经营和管护"
                                        }]
                                },
                                {
                                    "ids": "a024",
                                    "names": "木材和竹材采运",
                                    "childrens": [
                                        {
                                            "ids": "a0241",
                                            "names": "木材采运"
                                        },
                                        {
                                            "ids": "a0242",
                                            "names": "竹材采运"
                                        }]
                                },
                                {
                                    "ids": "a025",
                                    "names": "林产品采集",
                                    "childrens": [
                                        {
                                            "ids": "a0251",
                                            "names": "木竹材林产品采集"
                                        },
                                        {
                                            "ids": "a0252",
                                            "names": "非木竹材林产品采集"
                                        }]
                                }]
                        },
                        {
                            "ids": "a03",
                            "names": "畜牧业",
                            "childrens": [
                                {
                                    "ids": "a031",
                                    "names": "牲畜饲养",
                                    "childrens": [
                                        {
                                            "ids": "a0311",
                                            "names": "牛的饲养"
                                        },
                                        {
                                            "ids": "a0312",
                                            "names": "马的饲养"
                                        },
                                        {
                                            "ids": "a0313",
                                            "names": "猪的饲养"
                                        },
                                        {
                                            "ids": "a0314",
                                            "names": "羊的饲养"
                                        },
                                        {
                                            "ids": "a0315",
                                            "names": "骆驼饲养"
                                        },
                                        {
                                            "ids": "a0319",
                                            "names": "其他牲畜饲养"
                                        }]
                                },
                                {
                                    "ids": "a032",
                                    "names": "家禽饲养",
                                    "childrens": [
                                        {
                                            "ids": "a0321",
                                            "names": "鸡的饲养"
                                        },
                                        {
                                            "ids": "a0322",
                                            "names": "鸭的饲养"
                                        },
                                        {
                                            "ids": "a0323",
                                            "names": "鹅的饲养"
                                        },
                                        {
                                            "ids": "a0329",
                                            "names": "其他家禽饲养"
                                        }]
                                },
                                {
                                    "ids": "a033",
                                    "names": "狩猎和捕捉动物",
                                    "childrens": [
                                        {
                                            "ids": "a0330",
                                            "names": "狩猎和捕捉动物"
                                        }]
                                },
                                {
                                    "ids": "a039",
                                    "names": "其他畜牧业",
                                    "childrens": [
                                        {
                                            "ids": "a0390",
                                            "names": "其他畜牧业"
                                        }]
                                }]
                        },
                        {
                            "ids": "a04",
                            "names": "渔业",
                            "childrens": [
                                {
                                    "ids": "a041",
                                    "names": "水产养殖",
                                    "childrens": [
                                        {
                                            "ids": "a0411",
                                            "names": "海水养殖"
                                        },
                                        {
                                            "ids": "a0412",
                                            "names": "内陆养殖"
                                        }]
                                },
                                {
                                    "ids": "a042",
                                    "names": "水产捕捞",
                                    "childrens": [
                                        {
                                            "ids": "a0421",
                                            "names": "海水捕捞"
                                        },
                                        {
                                            "ids": "a0422",
                                            "names": "内陆捕捞"
                                        }]
                                }]
                        },
                        {
                            "ids": "a05",
                            "names": "农、林、牧、渔服务业",
                            "childrens": [
                                {
                                    "ids": "a052",
                                    "names": "林业服务业",
                                    "childrens": [
                                        {
                                            "ids": "a0521",
                                            "names": "林业有害生物防治服务"
                                        },
                                        {
                                            "ids": "a0522",
                                            "names": "森林防火服务"
                                        },
                                        {
                                            "ids": "a0523",
                                            "names": "林产品初级加工服务"
                                        },
                                        {
                                            "ids": "a0529",
                                            "names": "其他林业服务"
                                        }]
                                },
                                {
                                    "ids": "a054",
                                    "names": "渔业服务业",
                                    "childrens": [
                                        {
                                            "ids": "a0540",
                                            "names": "渔业服务业"
                                        }]
                                },
                                {
                                    "ids": "a051",
                                    "names": "农业服务业",
                                    "childrens": [
                                        {
                                            "ids": "a0511",
                                            "names": "农业机械服务"
                                        },
                                        {
                                            "ids": "a0512",
                                            "names": "灌溉服务"
                                        },
                                        {
                                            "ids": "a0513",
                                            "names": "农产品初加工服务"
                                        },
                                        {
                                            "ids": "a0519",
                                            "names": "其他农业服务"
                                        }]
                                },
                                {
                                    "ids": "a053",
                                    "names": "畜牧服务业",
                                    "childrens": [
                                        {
                                            "ids": "a0530",
                                            "names": "畜牧服务业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "b",
                    "names": "采矿业",
                    "childrens": [
                        {
                            "ids": "b06",
                            "names": "煤炭开采和洗选业",
                            "childrens": [
                                {
                                    "ids": "b061",
                                    "names": "烟煤和无烟煤开采洗选",
                                    "childrens": [
                                        {
                                            "ids": "b0610",
                                            "names": "烟煤和无烟煤开采洗选"
                                        }]
                                },
                                {
                                    "ids": "b062",
                                    "names": "褐煤开采洗选",
                                    "childrens": [
                                        {
                                            "ids": "b0620",
                                            "names": "褐煤开采洗选"
                                        }]
                                },
                                {
                                    "ids": "b069",
                                    "names": "其他煤炭采选",
                                    "childrens": [
                                        {
                                            "ids": "b0690",
                                            "names": "其他煤炭采选"
                                        }]
                                }]
                        },
                        {
                            "ids": "b07",
                            "names": "石油和天然气开采业",
                            "childrens": [
                                {
                                    "ids": "b071",
                                    "names": "石油开采",
                                    "childrens": [
                                        {
                                            "ids": "b0710",
                                            "names": "石油开采"
                                        }]
                                },
                                {
                                    "ids": "b072",
                                    "names": "天然气开采",
                                    "childrens": [
                                        {
                                            "ids": "b0720",
                                            "names": "天然气开采"
                                        }]
                                }]
                        },
                        {
                            "ids": "b08",
                            "names": "黑色金属矿采选业",
                            "childrens": [
                                {
                                    "ids": "b082",
                                    "names": "锰矿、铬矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0820",
                                            "names": "锰矿、铬矿采选"
                                        }]
                                },
                                {
                                    "ids": "b089",
                                    "names": "其他黑色金属矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0890",
                                            "names": "其他黑色金属矿采选"
                                        }]
                                },
                                {
                                    "ids": "b081",
                                    "names": "铁矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0810",
                                            "names": "铁矿采选"
                                        }]
                                }]
                        },
                        {
                            "ids": "b09",
                            "names": "有色金属矿采选业",
                            "childrens": [
                                {
                                    "ids": "b091",
                                    "names": "常用有色金属矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0911",
                                            "names": "铜矿采选"
                                        },
                                        {
                                            "ids": "b0912",
                                            "names": "铅锌矿采选"
                                        },
                                        {
                                            "ids": "b0913",
                                            "names": "镍钴矿采选"
                                        },
                                        {
                                            "ids": "b0914",
                                            "names": "锡矿采选"
                                        },
                                        {
                                            "ids": "b0915",
                                            "names": "锑矿采选"
                                        },
                                        {
                                            "ids": "b0916",
                                            "names": "铝矿采选"
                                        },
                                        {
                                            "ids": "b0917",
                                            "names": "镁矿采选"
                                        },
                                        {
                                            "ids": "b0919",
                                            "names": "其他常用有色金属矿采选"
                                        }]
                                },
                                {
                                    "ids": "b092",
                                    "names": "贵金属矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0921",
                                            "names": "金矿采选"
                                        },
                                        {
                                            "ids": "b0922",
                                            "names": "银矿采选"
                                        },
                                        {
                                            "ids": "b0929",
                                            "names": "其他贵金属矿采选"
                                        }]
                                },
                                {
                                    "ids": "b093",
                                    "names": "稀有稀土金属矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b0931",
                                            "names": "钨钼矿采选"
                                        },
                                        {
                                            "ids": "b0932",
                                            "names": "稀土金属矿采选"
                                        },
                                        {
                                            "ids": "b0933",
                                            "names": "放射性金属矿采选"
                                        },
                                        {
                                            "ids": "b0939",
                                            "names": "其他稀有金属矿采选"
                                        }]
                                }]
                        },
                        {
                            "ids": "b10",
                            "names": "非金属矿采选业",
                            "childrens": [
                                {
                                    "ids": "b101",
                                    "names": "土砂石开采",
                                    "childrens": [
                                        {
                                            "ids": "b1011",
                                            "names": "石灰石、石膏开采"
                                        },
                                        {
                                            "ids": "b1012",
                                            "names": "建筑装饰用石开采"
                                        },
                                        {
                                            "ids": "b1013",
                                            "names": "耐火土石开采"
                                        },
                                        {
                                            "ids": "b1019",
                                            "names": "粘土及其他土砂石开采"
                                        }]
                                },
                                {
                                    "ids": "b102",
                                    "names": "化学矿开采",
                                    "childrens": [
                                        {
                                            "ids": "b1020",
                                            "names": "化学矿开采"
                                        }]
                                },
                                {
                                    "ids": "b103",
                                    "names": "采盐",
                                    "childrens": [
                                        {
                                            "ids": "b1030",
                                            "names": "采盐"
                                        }]
                                },
                                {
                                    "ids": "b109",
                                    "names": "石棉及其他非金属矿采选",
                                    "childrens": [
                                        {
                                            "ids": "b1091",
                                            "names": "石棉、云母矿采选"
                                        },
                                        {
                                            "ids": "b1092",
                                            "names": "石墨、滑石采选"
                                        },
                                        {
                                            "ids": "b1093",
                                            "names": "宝石、玉石采选"
                                        },
                                        {
                                            "ids": "b1099",
                                            "names": "其他未列明非金属矿采选"
                                        }]
                                }]
                        },
                        {
                            "ids": "b11",
                            "names": "开采辅助活动",
                            "childrens": [
                                {
                                    "ids": "b111",
                                    "names": "煤炭开采和洗选辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "b1110",
                                            "names": "煤炭开采和洗选辅助活动"
                                        }]
                                },
                                {
                                    "ids": "b112",
                                    "names": "石油和天然气开采辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "b1120",
                                            "names": "石油和天然气开采辅助活动"
                                        }]
                                },
                                {
                                    "ids": "b119",
                                    "names": "其他开采辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "b1190",
                                            "names": "其他开采辅助活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "b12",
                            "names": "其他采矿业",
                            "childrens": [
                                {
                                    "ids": "b120",
                                    "names": "其他采矿业",
                                    "childrens": [
                                        {
                                            "ids": "b1200",
                                            "names": "其他采矿业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "c",
                    "names": "制造业",
                    "childrens": [
                        {
                            "ids": "c13",
                            "names": "农副食品加工业",
                            "childrens": [
                                {
                                    "ids": "c131",
                                    "names": "谷物磨制",
                                    "childrens": [
                                        {
                                            "ids": "c1310",
                                            "names": "谷物磨制"
                                        }]
                                },
                                {
                                    "ids": "c132",
                                    "names": "饲料加工",
                                    "childrens": [
                                        {
                                            "ids": "c1320",
                                            "names": "饲料加工"
                                        }]
                                },
                                {
                                    "ids": "c133",
                                    "names": "植物油加工",
                                    "childrens": [
                                        {
                                            "ids": "c1331",
                                            "names": "食用植物油加工"
                                        },
                                        {
                                            "ids": "c1332",
                                            "names": "非食用植物油加工"
                                        }]
                                },
                                {
                                    "ids": "c134",
                                    "names": "制糖业",
                                    "childrens": [
                                        {
                                            "ids": "c1340",
                                            "names": "制糖业"
                                        }]
                                },
                                {
                                    "ids": "c135",
                                    "names": "屠宰及肉类加工",
                                    "childrens": [
                                        {
                                            "ids": "c1351",
                                            "names": "牲畜屠宰"
                                        },
                                        {
                                            "ids": "c1352",
                                            "names": "禽类屠宰"
                                        },
                                        {
                                            "ids": "c1353",
                                            "names": "肉制品及副产品加工"
                                        }]
                                },
                                {
                                    "ids": "c136",
                                    "names": "水产品加工",
                                    "childrens": [
                                        {
                                            "ids": "c1361",
                                            "names": "水产品冷冻加工"
                                        },
                                        {
                                            "ids": "c1362",
                                            "names": "鱼糜制品及水产品干腌制加工"
                                        },
                                        {
                                            "ids": "c1363",
                                            "names": "水产饲料制造"
                                        },
                                        {
                                            "ids": "c1364",
                                            "names": "鱼油提取及制品制造"
                                        },
                                        {
                                            "ids": "c1369",
                                            "names": "其他水产品加工"
                                        }]
                                },
                                {
                                    "ids": "c137",
                                    "names": "蔬菜、水果和坚果加工",
                                    "childrens": [
                                        {
                                            "ids": "c1371",
                                            "names": "蔬菜加工"
                                        },
                                        {
                                            "ids": "c1372",
                                            "names": "水果和坚果加工"
                                        }]
                                },
                                {
                                    "ids": "c139",
                                    "names": "其他农副食品加工",
                                    "childrens": [
                                        {
                                            "ids": "c1391",
                                            "names": "淀粉及淀粉制品制造"
                                        },
                                        {
                                            "ids": "c1392",
                                            "names": "豆制品制造"
                                        },
                                        {
                                            "ids": "c1393",
                                            "names": "蛋品加工"
                                        },
                                        {
                                            "ids": "c1399",
                                            "names": "其他未列明农副食品加工"
                                        }]
                                }]
                        },
                        {
                            "ids": "c14",
                            "names": "食品制造业",
                            "childrens": [
                                {
                                    "ids": "c141",
                                    "names": "焙烤食品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1411",
                                            "names": "糕点、面包制造"
                                        },
                                        {
                                            "ids": "c1419",
                                            "names": "饼干及其他焙烤食品制造"
                                        }]
                                },
                                {
                                    "ids": "c142",
                                    "names": "糖果、巧克力及蜜饯制造",
                                    "childrens": [
                                        {
                                            "ids": "c1421",
                                            "names": "糖果、巧克力制造"
                                        },
                                        {
                                            "ids": "c1422",
                                            "names": "蜜饯制作"
                                        }]
                                },
                                {
                                    "ids": "c143",
                                    "names": "方便食品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1431",
                                            "names": "米、面制品制造"
                                        },
                                        {
                                            "ids": "c1432",
                                            "names": "速冻食品制造"
                                        },
                                        {
                                            "ids": "c1439",
                                            "names": "方便面及其他方便食品制造"
                                        }]
                                },
                                {
                                    "ids": "c144",
                                    "names": "乳制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1440",
                                            "names": "乳制品制造"
                                        }]
                                },
                                {
                                    "ids": "c145",
                                    "names": "罐头食品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1451",
                                            "names": "肉、禽类罐头制造"
                                        },
                                        {
                                            "ids": "c1452",
                                            "names": "水产品罐头制造"
                                        },
                                        {
                                            "ids": "c1453",
                                            "names": "蔬菜、水果罐头制造"
                                        },
                                        {
                                            "ids": "c1459",
                                            "names": "其他罐头食品制造"
                                        }]
                                },
                                {
                                    "ids": "c146",
                                    "names": "调味品、发酵制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1461",
                                            "names": "味精制造"
                                        },
                                        {
                                            "ids": "c1462",
                                            "names": "酱油、食醋及类似制品制造"
                                        },
                                        {
                                            "ids": "c1469",
                                            "names": "其他调味品、发酵制品制造"
                                        }]
                                },
                                {
                                    "ids": "c149",
                                    "names": "其他食品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1491",
                                            "names": "营养食品制造"
                                        },
                                        {
                                            "ids": "c1492",
                                            "names": "保健食品制造"
                                        },
                                        {
                                            "ids": "c1494",
                                            "names": "盐加工"
                                        },
                                        {
                                            "ids": "c1495",
                                            "names": "食品及饲料添加剂制造"
                                        },
                                        {
                                            "ids": "c1499",
                                            "names": "其他未列明食品制造"
                                        },
                                        {
                                            "ids": "c1493",
                                            "names": "冷冻饮品及食用冰制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c15",
                            "names": "酒、饮料和精制茶制造业",
                            "childrens": [
                                {
                                    "ids": "c152",
                                    "names": "饮料制造",
                                    "childrens": [
                                        {
                                            "ids": "c1521",
                                            "names": "碳酸饮料制造"
                                        },
                                        {
                                            "ids": "c1522",
                                            "names": "瓶（罐）装饮用水制造"
                                        },
                                        {
                                            "ids": "c1523",
                                            "names": "果菜汁及果菜汁饮料制造"
                                        },
                                        {
                                            "ids": "c1524",
                                            "names": "含乳饮料和植物蛋白饮料制造"
                                        },
                                        {
                                            "ids": "c1525",
                                            "names": "固体饮料制造"
                                        },
                                        {
                                            "ids": "c1529",
                                            "names": "茶饮料及其他饮料制造"
                                        }]
                                },
                                {
                                    "ids": "c151",
                                    "names": "酒的制造",
                                    "childrens": [
                                        {
                                            "ids": "c1513",
                                            "names": "啤酒制造"
                                        },
                                        {
                                            "ids": "c1514",
                                            "names": "黄酒制造"
                                        },
                                        {
                                            "ids": "c1515",
                                            "names": "葡萄酒制造"
                                        },
                                        {
                                            "ids": "c1519",
                                            "names": "其他酒制造"
                                        },
                                        {
                                            "ids": "c1511",
                                            "names": "酒精制造"
                                        },
                                        {
                                            "ids": "c1512",
                                            "names": "白酒制造"
                                        }]
                                },
                                {
                                    "ids": "c153",
                                    "names": "精制茶加工",
                                    "childrens": [
                                        {
                                            "ids": "c1530",
                                            "names": "精制茶加工"
                                        }]
                                }]
                        },
                        {
                            "ids": "c16",
                            "names": "烟草制品业",
                            "childrens": [
                                {
                                    "ids": "c161",
                                    "names": "烟叶复烤",
                                    "childrens": [
                                        {
                                            "ids": "c1610",
                                            "names": "烟叶复烤"
                                        }]
                                },
                                {
                                    "ids": "c162",
                                    "names": "卷烟制造",
                                    "childrens": [
                                        {
                                            "ids": "c1620",
                                            "names": "卷烟制造"
                                        }]
                                },
                                {
                                    "ids": "c169",
                                    "names": "其他烟草制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1690",
                                            "names": "其他烟草制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c17",
                            "names": "纺织业",
                            "childrens": [
                                {
                                    "ids": "c171",
                                    "names": "棉纺织及印染精加工",
                                    "childrens": [
                                        {
                                            "ids": "c1711",
                                            "names": "棉纺纱加工"
                                        },
                                        {
                                            "ids": "c1712",
                                            "names": "棉织造加工"
                                        },
                                        {
                                            "ids": "c1713",
                                            "names": "棉印染精加工"
                                        }]
                                },
                                {
                                    "ids": "c172",
                                    "names": "毛纺织及染整精加工",
                                    "childrens": [
                                        {
                                            "ids": "c1721",
                                            "names": "毛条和毛纱线加工"
                                        },
                                        {
                                            "ids": "c1722",
                                            "names": "毛织造加工"
                                        },
                                        {
                                            "ids": "c1723",
                                            "names": "毛染整精加工"
                                        }]
                                },
                                {
                                    "ids": "c173",
                                    "names": "麻纺织及染整精加工",
                                    "childrens": [
                                        {
                                            "ids": "c1731",
                                            "names": "麻纤维纺前加工和纺纱"
                                        },
                                        {
                                            "ids": "c1732",
                                            "names": "麻织造加工"
                                        },
                                        {
                                            "ids": "c1733",
                                            "names": "麻染整精加工"
                                        }]
                                },
                                {
                                    "ids": "c174",
                                    "names": "丝绢纺织及印染精加工",
                                    "childrens": [
                                        {
                                            "ids": "c1741",
                                            "names": "缫丝加工"
                                        },
                                        {
                                            "ids": "c1742",
                                            "names": "绢纺和丝织加工"
                                        },
                                        {
                                            "ids": "c1743",
                                            "names": "丝印染精加工"
                                        }]
                                },
                                {
                                    "ids": "c175",
                                    "names": "化纤织造及印染精加工",
                                    "childrens": [
                                        {
                                            "ids": "c1751",
                                            "names": "化纤织造加工"
                                        },
                                        {
                                            "ids": "c1752",
                                            "names": "化纤织物染整精加工"
                                        }]
                                },
                                {
                                    "ids": "c176",
                                    "names": "针织或钩针编织物及其制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1761",
                                            "names": "针织或钩针编织物织造"
                                        },
                                        {
                                            "ids": "c1762",
                                            "names": "针织或钩针编织物印染精加工"
                                        },
                                        {
                                            "ids": "c1763",
                                            "names": "针织或钩针编织品制造"
                                        }]
                                },
                                {
                                    "ids": "c177",
                                    "names": "家用纺织制成品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1771",
                                            "names": "床上用品制造"
                                        },
                                        {
                                            "ids": "c1772",
                                            "names": "毛巾类制品制造"
                                        },
                                        {
                                            "ids": "c1773",
                                            "names": "窗帘、布艺类产品制造"
                                        },
                                        {
                                            "ids": "c1779",
                                            "names": "其他家用纺织制成品制造"
                                        }]
                                },
                                {
                                    "ids": "c178",
                                    "names": "非家用纺织制成品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1781",
                                            "names": "非织造布制造"
                                        },
                                        {
                                            "ids": "c1782",
                                            "names": "绳、索、缆制造"
                                        },
                                        {
                                            "ids": "c1783",
                                            "names": "纺织带和帘子布制造"
                                        },
                                        {
                                            "ids": "c1784",
                                            "names": "篷、帆布制造"
                                        },
                                        {
                                            "ids": "c1789",
                                            "names": "其他非家用纺织制成品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c18",
                            "names": "纺织服装、服饰业",
                            "childrens": [
                                {
                                    "ids": "c181",
                                    "names": "机织服装制造",
                                    "childrens": [
                                        {
                                            "ids": "c1810",
                                            "names": "机织服装制造"
                                        }]
                                },
                                {
                                    "ids": "c182",
                                    "names": "针织或钩针编织服装制造",
                                    "childrens": [
                                        {
                                            "ids": "c1820",
                                            "names": "针织或钩针编织服装制造"
                                        }]
                                },
                                {
                                    "ids": "c183",
                                    "names": "服饰制造",
                                    "childrens": [
                                        {
                                            "ids": "c1830",
                                            "names": "服饰制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c19",
                            "names": "皮革、毛皮、羽毛及其制品和制鞋业",
                            "childrens": [
                                {
                                    "ids": "c191",
                                    "names": "皮革鞣制加工",
                                    "childrens": [
                                        {
                                            "ids": "c1910",
                                            "names": "皮革鞣制加工"
                                        }]
                                },
                                {
                                    "ids": "c192",
                                    "names": "皮革制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1921",
                                            "names": "皮革服装制造"
                                        },
                                        {
                                            "ids": "c1922",
                                            "names": "皮箱、包（袋）制造"
                                        },
                                        {
                                            "ids": "c1923",
                                            "names": "皮手套及皮装饰制品制造"
                                        },
                                        {
                                            "ids": "c1929",
                                            "names": "其他皮革制品制造"
                                        }]
                                },
                                {
                                    "ids": "c193",
                                    "names": "毛皮鞣制及制品加工",
                                    "childrens": [
                                        {
                                            "ids": "c1931",
                                            "names": "毛皮鞣制加工"
                                        },
                                        {
                                            "ids": "c1932",
                                            "names": "毛皮服装加工"
                                        },
                                        {
                                            "ids": "c1939",
                                            "names": "其他毛皮制品加工"
                                        }]
                                },
                                {
                                    "ids": "c194",
                                    "names": "羽毛(绒)加工及制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c1941",
                                            "names": "羽毛（绒）加工"
                                        },
                                        {
                                            "ids": "c1942",
                                            "names": "羽毛（绒）制品加工"
                                        }]
                                },
                                {
                                    "ids": "c195",
                                    "names": "制鞋业",
                                    "childrens": [
                                        {
                                            "ids": "c1951",
                                            "names": "纺织面料鞋制造"
                                        },
                                        {
                                            "ids": "c1952",
                                            "names": "皮鞋制造"
                                        },
                                        {
                                            "ids": "c1953",
                                            "names": "塑料鞋制造"
                                        },
                                        {
                                            "ids": "c1954",
                                            "names": "橡胶鞋制造"
                                        },
                                        {
                                            "ids": "c1959",
                                            "names": "其他制鞋业"
                                        }]
                                }]
                        },
                        {
                            "ids": "c20",
                            "names": "木材加工和木、竹、藤、棕、草制品业",
                            "childrens": [
                                {
                                    "ids": "c201",
                                    "names": "木材加工",
                                    "childrens": [
                                        {
                                            "ids": "c2011",
                                            "names": "锯材加工"
                                        },
                                        {
                                            "ids": "c2012",
                                            "names": "木片加工"
                                        },
                                        {
                                            "ids": "c2013",
                                            "names": "单板加工"
                                        },
                                        {
                                            "ids": "c2019",
                                            "names": "其他木材加工"
                                        }]
                                },
                                {
                                    "ids": "c202",
                                    "names": "人造板制造",
                                    "childrens": [
                                        {
                                            "ids": "c2021",
                                            "names": "胶合板制造"
                                        },
                                        {
                                            "ids": "c2022",
                                            "names": "纤维板制造"
                                        },
                                        {
                                            "ids": "c2023",
                                            "names": "刨花板制造"
                                        },
                                        {
                                            "ids": "c2029",
                                            "names": "其他人造板制造"
                                        }]
                                },
                                {
                                    "ids": "c204",
                                    "names": "竹、藤、棕、草等制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2041",
                                            "names": "竹制品制造"
                                        },
                                        {
                                            "ids": "c2042",
                                            "names": "藤制品制造"
                                        },
                                        {
                                            "ids": "c2043",
                                            "names": "棕制品制造"
                                        },
                                        {
                                            "ids": "c2049",
                                            "names": "草及其他制品制造"
                                        }]
                                },
                                {
                                    "ids": "c203",
                                    "names": "木制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2031",
                                            "names": "建筑用木料及木材组件加工"
                                        },
                                        {
                                            "ids": "c2032",
                                            "names": "木门窗、楼梯制造"
                                        },
                                        {
                                            "ids": "c2033",
                                            "names": "地板制造"
                                        },
                                        {
                                            "ids": "c2034",
                                            "names": "木制容器制造"
                                        },
                                        {
                                            "ids": "c2039",
                                            "names": "软木制品及其他木制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c21",
                            "names": "家具制造业",
                            "childrens": [
                                {
                                    "ids": "c211",
                                    "names": "木质家具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2110",
                                            "names": "木质家具制造"
                                        }]
                                },
                                {
                                    "ids": "c212",
                                    "names": "竹、藤家具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2120",
                                            "names": "竹、藤家具制造"
                                        }]
                                },
                                {
                                    "ids": "c213",
                                    "names": "金属家具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2130",
                                            "names": "金属家具制造"
                                        }]
                                },
                                {
                                    "ids": "c214",
                                    "names": "塑料家具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2140",
                                            "names": "塑料家具制造"
                                        }]
                                },
                                {
                                    "ids": "c219",
                                    "names": "其他家具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2190",
                                            "names": "其他家具制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c22",
                            "names": "造纸和纸制品业",
                            "childrens": [
                                {
                                    "ids": "c221",
                                    "names": "纸浆制造",
                                    "childrens": [
                                        {
                                            "ids": "c2211",
                                            "names": "木竹浆制造"
                                        },
                                        {
                                            "ids": "c2212",
                                            "names": "非木竹浆制造"
                                        }]
                                },
                                {
                                    "ids": "c222",
                                    "names": "造纸",
                                    "childrens": [
                                        {
                                            "ids": "c2221",
                                            "names": "机制纸及纸板制造"
                                        },
                                        {
                                            "ids": "c2222",
                                            "names": "手工纸制造"
                                        },
                                        {
                                            "ids": "c2223",
                                            "names": "加工纸制造"
                                        }]
                                },
                                {
                                    "ids": "c223",
                                    "names": "纸制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2231",
                                            "names": "纸和纸板容器制造"
                                        },
                                        {
                                            "ids": "c2239",
                                            "names": "其他纸制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c23",
                            "names": "印刷和记录媒介复制业",
                            "childrens": [
                                {
                                    "ids": "c231",
                                    "names": "印刷",
                                    "childrens": [
                                        {
                                            "ids": "c2311",
                                            "names": "书、报刊印刷"
                                        },
                                        {
                                            "ids": "c2312",
                                            "names": "本册印制"
                                        },
                                        {
                                            "ids": "c2319",
                                            "names": "包装装潢及其他印刷"
                                        }]
                                },
                                {
                                    "ids": "c232",
                                    "names": "装订及印刷相关服务",
                                    "childrens": [
                                        {
                                            "ids": "c2320",
                                            "names": "装订及印刷相关服务"
                                        }]
                                },
                                {
                                    "ids": "c233",
                                    "names": "记录媒介复制",
                                    "childrens": [
                                        {
                                            "ids": "c2330",
                                            "names": "记录媒介复制"
                                        }]
                                }]
                        },
                        {
                            "ids": "c24",
                            "names": "文教、工美、体育和娱乐用品制造业",
                            "childrens": [
                                {
                                    "ids": "c241",
                                    "names": "文教办公用品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2411",
                                            "names": "文具制造"
                                        },
                                        {
                                            "ids": "c2412",
                                            "names": "笔的制造"
                                        },
                                        {
                                            "ids": "c2413",
                                            "names": "教学用模型及教具制造"
                                        },
                                        {
                                            "ids": "c2414",
                                            "names": "墨水、墨汁制造"
                                        },
                                        {
                                            "ids": "c2419",
                                            "names": "其他文教办公用品制造"
                                        }]
                                },
                                {
                                    "ids": "c242",
                                    "names": "乐器制造",
                                    "childrens": [
                                        {
                                            "ids": "c2421",
                                            "names": "中乐器制造"
                                        },
                                        {
                                            "ids": "c2422",
                                            "names": "西乐器制造"
                                        },
                                        {
                                            "ids": "c2423",
                                            "names": "电子乐器制造"
                                        },
                                        {
                                            "ids": "c2429",
                                            "names": "其他乐器及零件制造"
                                        }]
                                },
                                {
                                    "ids": "c243",
                                    "names": "工艺美术品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2431",
                                            "names": "雕塑工艺品制造"
                                        },
                                        {
                                            "ids": "c2432",
                                            "names": "金属工艺品制造"
                                        },
                                        {
                                            "ids": "c2433",
                                            "names": "漆器工艺品制造"
                                        },
                                        {
                                            "ids": "c2434",
                                            "names": "花画工艺品制造"
                                        },
                                        {
                                            "ids": "c2435",
                                            "names": "天然植物纤维编织工艺品制造"
                                        },
                                        {
                                            "ids": "c2436",
                                            "names": "抽纱刺绣工艺品制造"
                                        },
                                        {
                                            "ids": "c2437",
                                            "names": "地毯、挂毯制造"
                                        },
                                        {
                                            "ids": "c2438",
                                            "names": "珠宝首饰及有关物品制造"
                                        },
                                        {
                                            "ids": "c2439",
                                            "names": "其他工艺美术品制造"
                                        }]
                                },
                                {
                                    "ids": "c244",
                                    "names": "体育用品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2441",
                                            "names": "球类制造"
                                        },
                                        {
                                            "ids": "c2442",
                                            "names": "体育器材及配件制造"
                                        },
                                        {
                                            "ids": "c2443",
                                            "names": "训练健身器材制造"
                                        },
                                        {
                                            "ids": "c2444",
                                            "names": "运动防护用具制造"
                                        },
                                        {
                                            "ids": "c2449",
                                            "names": "其他体育用品制造"
                                        }]
                                },
                                {
                                    "ids": "c245",
                                    "names": "玩具制造",
                                    "childrens": [
                                        {
                                            "ids": "c2450",
                                            "names": "玩具制造"
                                        }]
                                },
                                {
                                    "ids": "c246",
                                    "names": "游艺器材及娱乐用品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2461",
                                            "names": "露天游乐场所游乐设备制造"
                                        },
                                        {
                                            "ids": "c2462",
                                            "names": "游艺用品及室内游艺器材制造"
                                        },
                                        {
                                            "ids": "c2469",
                                            "names": "其他娱乐用品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c25",
                            "names": "石油加工、炼焦和核燃料加工业",
                            "childrens": [
                                {
                                    "ids": "c251",
                                    "names": "精炼石油产品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2511",
                                            "names": "原油加工及石油制品制造"
                                        },
                                        {
                                            "ids": "c2512",
                                            "names": "人造原油制造"
                                        }]
                                },
                                {
                                    "ids": "c252",
                                    "names": "炼焦",
                                    "childrens": [
                                        {
                                            "ids": "c2520",
                                            "names": "炼焦"
                                        }]
                                },
                                {
                                    "ids": "c253",
                                    "names": "核燃料加工",
                                    "childrens": [
                                        {
                                            "ids": "c2530",
                                            "names": "核燃料加工"
                                        }]
                                }]
                        },
                        {
                            "ids": "c26",
                            "names": "化学原料和化学制品制造业",
                            "childrens": [
                                {
                                    "ids": "c261",
                                    "names": "基础化学原料制造",
                                    "childrens": [
                                        {
                                            "ids": "c2611",
                                            "names": "无机酸制造"
                                        },
                                        {
                                            "ids": "c2612",
                                            "names": "无机碱制造"
                                        },
                                        {
                                            "ids": "c2613",
                                            "names": "无机盐制造"
                                        },
                                        {
                                            "ids": "c2614",
                                            "names": "有机化学原料制造"
                                        },
                                        {
                                            "ids": "c2619",
                                            "names": "其他基础化学原料制造"
                                        }]
                                },
                                {
                                    "ids": "c262",
                                    "names": "肥料制造",
                                    "childrens": [
                                        {
                                            "ids": "c2625",
                                            "names": "有机肥料及微生物肥料制造"
                                        },
                                        {
                                            "ids": "c2629",
                                            "names": "其他肥料制造"
                                        },
                                        {
                                            "ids": "c2621",
                                            "names": "氮肥制造"
                                        },
                                        {
                                            "ids": "c2622",
                                            "names": "磷肥制造"
                                        },
                                        {
                                            "ids": "c2623",
                                            "names": "钾肥制造"
                                        },
                                        {
                                            "ids": "c2624",
                                            "names": "复混肥料制造"
                                        }]
                                },
                                {
                                    "ids": "c263",
                                    "names": "农药制造",
                                    "childrens": [
                                        {
                                            "ids": "c2631",
                                            "names": "化学农药制造"
                                        },
                                        {
                                            "ids": "c2632",
                                            "names": "生物化学农药及微生物农药制造"
                                        }]
                                },
                                {
                                    "ids": "c264",
                                    "names": "涂料、油墨、颜料及类似产品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2641",
                                            "names": "涂料制造"
                                        },
                                        {
                                            "ids": "c2642",
                                            "names": "油墨及类似产品制造"
                                        },
                                        {
                                            "ids": "c2643",
                                            "names": "颜料制造"
                                        },
                                        {
                                            "ids": "c2644",
                                            "names": "染料制造"
                                        },
                                        {
                                            "ids": "c2645",
                                            "names": "密封用填料及类似品制造"
                                        }]
                                },
                                {
                                    "ids": "c265",
                                    "names": "合成材料制造",
                                    "childrens": [
                                        {
                                            "ids": "c2651",
                                            "names": "初级形态塑料及合成树脂制造"
                                        },
                                        {
                                            "ids": "c2652",
                                            "names": "合成橡胶制造"
                                        },
                                        {
                                            "ids": "c2653",
                                            "names": "合成纤维单（聚合）体制造"
                                        },
                                        {
                                            "ids": "c2659",
                                            "names": "其他合成材料制造"
                                        }]
                                },
                                {
                                    "ids": "c266",
                                    "names": "专用化学产品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2661",
                                            "names": "化学试剂和助剂制造"
                                        },
                                        {
                                            "ids": "c2662",
                                            "names": "专项化学用品制造"
                                        },
                                        {
                                            "ids": "c2663",
                                            "names": "林产化学产品制造"
                                        },
                                        {
                                            "ids": "c2664",
                                            "names": "信息化学品制造"
                                        },
                                        {
                                            "ids": "c2665",
                                            "names": "环境污染处理专用药剂材料制造"
                                        },
                                        {
                                            "ids": "c2666",
                                            "names": "动物胶制造"
                                        },
                                        {
                                            "ids": "c2669",
                                            "names": "其他专用化学产品制造"
                                        }]
                                },
                                {
                                    "ids": "c267",
                                    "names": "炸药、火工及焰火产品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2671",
                                            "names": "炸药及火工产品制造"
                                        },
                                        {
                                            "ids": "c2672",
                                            "names": "焰火、鞭炮产品制造"
                                        }]
                                },
                                {
                                    "ids": "c268",
                                    "names": "日用化学产品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2681",
                                            "names": "肥皂及合成洗涤剂制造"
                                        },
                                        {
                                            "ids": "c2682",
                                            "names": "化妆品制造"
                                        },
                                        {
                                            "ids": "c2683",
                                            "names": "口腔清洁用品制造"
                                        },
                                        {
                                            "ids": "c2684",
                                            "names": "香料、香精制造"
                                        },
                                        {
                                            "ids": "c2689",
                                            "names": "其他日用化学产品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c27",
                            "names": "医药制造业",
                            "childrens": [
                                {
                                    "ids": "c271",
                                    "names": "化学药品原料药制造",
                                    "childrens": [
                                        {
                                            "ids": "c2710",
                                            "names": "化学药品原料药制造"
                                        }]
                                },
                                {
                                    "ids": "c272",
                                    "names": "化学药品制剂制造",
                                    "childrens": [
                                        {
                                            "ids": "c2720",
                                            "names": "化学药品制剂制造"
                                        }]
                                },
                                {
                                    "ids": "c273",
                                    "names": "中药饮片加工",
                                    "childrens": [
                                        {
                                            "ids": "c2730",
                                            "names": "中药饮片加工"
                                        }]
                                },
                                {
                                    "ids": "c274",
                                    "names": "中成药生产",
                                    "childrens": [
                                        {
                                            "ids": "c2740",
                                            "names": "中成药生产"
                                        }]
                                },
                                {
                                    "ids": "c275",
                                    "names": "兽用药品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2750",
                                            "names": "兽用药品制造"
                                        }]
                                },
                                {
                                    "ids": "c276",
                                    "names": "生物药品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2760",
                                            "names": "生物药品制造"
                                        }]
                                },
                                {
                                    "ids": "c277",
                                    "names": "卫生材料及医药用品制造",
                                    "childrens": [
                                        {
                                            "ids": "c2770",
                                            "names": "卫生材料及医药用品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c28",
                            "names": "化学纤维制造业",
                            "childrens": [
                                {
                                    "ids": "c281",
                                    "names": "纤维素纤维原料及纤维制造",
                                    "childrens": [
                                        {
                                            "ids": "c2811",
                                            "names": "化纤浆粕制造"
                                        },
                                        {
                                            "ids": "c2812",
                                            "names": "人造纤维（纤维素纤维）制造"
                                        }]
                                },
                                {
                                    "ids": "c282",
                                    "names": "合成纤维制造",
                                    "childrens": [
                                        {
                                            "ids": "c2821",
                                            "names": "锦纶纤维制造"
                                        },
                                        {
                                            "ids": "c2822",
                                            "names": "涤纶纤维制造"
                                        },
                                        {
                                            "ids": "c2823",
                                            "names": "腈纶纤维制造"
                                        },
                                        {
                                            "ids": "c2824",
                                            "names": "维纶纤维制造"
                                        },
                                        {
                                            "ids": "c2825",
                                            "names": "丙纶纤维制造"
                                        },
                                        {
                                            "ids": "c2826",
                                            "names": "氨纶纤维制造"
                                        },
                                        {
                                            "ids": "c2829",
                                            "names": "其他合成纤维制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c29",
                            "names": "橡胶和塑料制品业",
                            "childrens": [
                                {
                                    "ids": "c291",
                                    "names": "橡胶制品业",
                                    "childrens": [
                                        {
                                            "ids": "c2911",
                                            "names": "轮胎制造"
                                        },
                                        {
                                            "ids": "c2912",
                                            "names": "橡胶板、管、带制造"
                                        },
                                        {
                                            "ids": "c2913",
                                            "names": "橡胶零件制造"
                                        },
                                        {
                                            "ids": "c2914",
                                            "names": "再生橡胶制造"
                                        },
                                        {
                                            "ids": "c2915",
                                            "names": "日用及医用橡胶制品制造"
                                        },
                                        {
                                            "ids": "c2919",
                                            "names": "其他橡胶制品制造"
                                        }]
                                },
                                {
                                    "ids": "c292",
                                    "names": "塑料制品业",
                                    "childrens": [
                                        {
                                            "ids": "c2921",
                                            "names": "塑料薄膜制造"
                                        },
                                        {
                                            "ids": "c2922",
                                            "names": "塑料板、管、型材制造"
                                        },
                                        {
                                            "ids": "c2923",
                                            "names": "塑料丝、绳及编织品制造"
                                        },
                                        {
                                            "ids": "c2924",
                                            "names": "泡沫塑料制造"
                                        },
                                        {
                                            "ids": "c2925",
                                            "names": "塑料人造革、合成革制造"
                                        },
                                        {
                                            "ids": "c2926",
                                            "names": "塑料包装箱及容器制造"
                                        },
                                        {
                                            "ids": "c2927",
                                            "names": "日用塑料制品制造"
                                        },
                                        {
                                            "ids": "c2928",
                                            "names": "塑料零件制造"
                                        },
                                        {
                                            "ids": "c2929",
                                            "names": "其他塑料制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c30",
                            "names": "非金属矿物制品业",
                            "childrens": [
                                {
                                    "ids": "c301",
                                    "names": "水泥、石灰和石膏制造",
                                    "childrens": [
                                        {
                                            "ids": "c3011",
                                            "names": "水泥制造"
                                        },
                                        {
                                            "ids": "c3012",
                                            "names": "石灰和石膏制造"
                                        }]
                                },
                                {
                                    "ids": "c302",
                                    "names": "石膏、水泥制品及类似制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3021",
                                            "names": "水泥制品制造"
                                        },
                                        {
                                            "ids": "c3022",
                                            "names": "砼结构构件制造"
                                        },
                                        {
                                            "ids": "c3023",
                                            "names": "石棉水泥制品制造"
                                        },
                                        {
                                            "ids": "c3024",
                                            "names": "轻质建筑材料制造"
                                        },
                                        {
                                            "ids": "c3029",
                                            "names": "其他水泥类似制品制造"
                                        }]
                                },
                                {
                                    "ids": "c303",
                                    "names": "砖瓦、石材等建筑材料制造",
                                    "childrens": [
                                        {
                                            "ids": "c3031",
                                            "names": "粘土砖瓦及建筑砌块制造"
                                        },
                                        {
                                            "ids": "c3032",
                                            "names": "建筑陶瓷制品制造"
                                        },
                                        {
                                            "ids": "c3033",
                                            "names": "建筑用石加工"
                                        },
                                        {
                                            "ids": "c3034",
                                            "names": "防水建筑材料制造"
                                        },
                                        {
                                            "ids": "c3035",
                                            "names": "隔热和隔音材料制造"
                                        },
                                        {
                                            "ids": "c3039",
                                            "names": "其他建筑材料制造"
                                        }]
                                },
                                {
                                    "ids": "c304",
                                    "names": "玻璃制造",
                                    "childrens": [
                                        {
                                            "ids": "c3041",
                                            "names": "平板玻璃制造"
                                        },
                                        {
                                            "ids": "c3049",
                                            "names": "其他玻璃制造"
                                        }]
                                },
                                {
                                    "ids": "c305",
                                    "names": "玻璃制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3051",
                                            "names": "技术玻璃制品制造"
                                        },
                                        {
                                            "ids": "c3052",
                                            "names": "光学玻璃制造"
                                        },
                                        {
                                            "ids": "c3053",
                                            "names": "玻璃仪器制造"
                                        },
                                        {
                                            "ids": "c3054",
                                            "names": "日用玻璃制品制造"
                                        },
                                        {
                                            "ids": "c3055",
                                            "names": "玻璃包装容器制造"
                                        },
                                        {
                                            "ids": "c3056",
                                            "names": "玻璃保温容器制造"
                                        },
                                        {
                                            "ids": "c3057",
                                            "names": "制镜及类似品加工"
                                        },
                                        {
                                            "ids": "c3059",
                                            "names": "其他玻璃制品制造"
                                        }]
                                },
                                {
                                    "ids": "c306",
                                    "names": "玻璃纤维和玻璃纤维增强塑料制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3061",
                                            "names": "玻璃纤维及制品制造"
                                        },
                                        {
                                            "ids": "c3062",
                                            "names": "玻璃纤维增强塑料制品制造"
                                        }]
                                },
                                {
                                    "ids": "c307",
                                    "names": "陶瓷制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3071",
                                            "names": "卫生陶瓷制品制造"
                                        },
                                        {
                                            "ids": "c3072",
                                            "names": "特种陶瓷制品制造"
                                        },
                                        {
                                            "ids": "c3073",
                                            "names": "日用陶瓷制品制造"
                                        },
                                        {
                                            "ids": "c3079",
                                            "names": "园林、陈设艺术及其他陶瓷制品制造"
                                        }]
                                },
                                {
                                    "ids": "c308",
                                    "names": "耐火材料制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3089",
                                            "names": "耐火陶瓷制品及其他耐火材料制造"
                                        },
                                        {
                                            "ids": "c3081",
                                            "names": "石棉制品制造"
                                        },
                                        {
                                            "ids": "c3082",
                                            "names": "云母制品制造"
                                        }]
                                },
                                {
                                    "ids": "c309",
                                    "names": "石墨及其他非金属矿物制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3091",
                                            "names": "石墨及碳素制品制造"
                                        },
                                        {
                                            "ids": "c3099",
                                            "names": "其他非金属矿物制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c31",
                            "names": "黑色金属冶炼和压延加工业",
                            "childrens": [
                                {
                                    "ids": "c314",
                                    "names": "钢压延加工",
                                    "childrens": [
                                        {
                                            "ids": "c3140",
                                            "names": "钢压延加工"
                                        }]
                                },
                                {
                                    "ids": "c315",
                                    "names": "铁合金冶炼",
                                    "childrens": [
                                        {
                                            "ids": "c3150",
                                            "names": "铁合金冶炼"
                                        }]
                                },
                                {
                                    "ids": "c311",
                                    "names": "炼铁",
                                    "childrens": [
                                        {
                                            "ids": "c3110",
                                            "names": "炼铁"
                                        }]
                                },
                                {
                                    "ids": "c312",
                                    "names": "炼钢",
                                    "childrens": [
                                        {
                                            "ids": "c3120",
                                            "names": "炼钢"
                                        }]
                                },
                                {
                                    "ids": "c313",
                                    "names": "黑色金属铸造",
                                    "childrens": [
                                        {
                                            "ids": "c3130",
                                            "names": "黑色金属铸造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c32",
                            "names": "有色金属冶炼和压延加工业",
                            "childrens": [
                                {
                                    "ids": "c321",
                                    "names": "常用有色金属冶炼",
                                    "childrens": [
                                        {
                                            "ids": "c3211",
                                            "names": "铜冶炼"
                                        },
                                        {
                                            "ids": "c3212",
                                            "names": "铅锌冶炼"
                                        },
                                        {
                                            "ids": "c3213",
                                            "names": "镍钴冶炼"
                                        },
                                        {
                                            "ids": "c3214",
                                            "names": "锡冶炼"
                                        },
                                        {
                                            "ids": "c3215",
                                            "names": "锑冶炼"
                                        },
                                        {
                                            "ids": "c3216",
                                            "names": "铝冶炼"
                                        },
                                        {
                                            "ids": "c3217",
                                            "names": "镁冶炼"
                                        },
                                        {
                                            "ids": "c3219",
                                            "names": "其他常用有色金属冶炼"
                                        }]
                                },
                                {
                                    "ids": "c322",
                                    "names": "贵金属冶炼",
                                    "childrens": [
                                        {
                                            "ids": "c3221",
                                            "names": "金冶炼"
                                        },
                                        {
                                            "ids": "c3222",
                                            "names": "银冶炼"
                                        },
                                        {
                                            "ids": "c3229",
                                            "names": "其他贵金属冶炼"
                                        }]
                                },
                                {
                                    "ids": "c323",
                                    "names": "稀有稀土金属冶炼",
                                    "childrens": [
                                        {
                                            "ids": "c3231",
                                            "names": "钨钼冶炼"
                                        },
                                        {
                                            "ids": "c3232",
                                            "names": "稀土金属冶炼"
                                        },
                                        {
                                            "ids": "c3239",
                                            "names": "其他稀有金属冶炼"
                                        }]
                                },
                                {
                                    "ids": "c324",
                                    "names": "有色金属合金制造",
                                    "childrens": [
                                        {
                                            "ids": "c3240",
                                            "names": "有色金属合金制造"
                                        }]
                                },
                                {
                                    "ids": "c325",
                                    "names": "有色金属铸造",
                                    "childrens": [
                                        {
                                            "ids": "c3250",
                                            "names": "有色金属铸造"
                                        }]
                                },
                                {
                                    "ids": "c326",
                                    "names": "有色金属压延加工",
                                    "childrens": [
                                        {
                                            "ids": "c3261",
                                            "names": "铜压延加工"
                                        },
                                        {
                                            "ids": "c3262",
                                            "names": "铝压延加工"
                                        },
                                        {
                                            "ids": "c3263",
                                            "names": "贵金属压延加工"
                                        },
                                        {
                                            "ids": "c3264",
                                            "names": "稀有稀土金属压延加工"
                                        },
                                        {
                                            "ids": "c3269",
                                            "names": "其他有色金属压延加工"
                                        }]
                                }]
                        },
                        {
                            "ids": "c33",
                            "names": "金属制品业",
                            "childrens": [
                                {
                                    "ids": "c331",
                                    "names": "结构性金属制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3311",
                                            "names": "金属结构制造"
                                        },
                                        {
                                            "ids": "c3312",
                                            "names": "金属门窗制造"
                                        }]
                                },
                                {
                                    "ids": "c332",
                                    "names": "金属工具制造",
                                    "childrens": [
                                        {
                                            "ids": "c3321",
                                            "names": "切削工具制造"
                                        },
                                        {
                                            "ids": "c3322",
                                            "names": "手工具制造"
                                        },
                                        {
                                            "ids": "c3323",
                                            "names": "农用及园林用金属工具制造"
                                        },
                                        {
                                            "ids": "c3324",
                                            "names": "刀剪及类似日用金属工具制造"
                                        },
                                        {
                                            "ids": "c3329",
                                            "names": "其他金属工具制造"
                                        }]
                                },
                                {
                                    "ids": "c333",
                                    "names": "集装箱及金属包装容器制造",
                                    "childrens": [
                                        {
                                            "ids": "c3331",
                                            "names": "集装箱制造"
                                        },
                                        {
                                            "ids": "c3332",
                                            "names": "金属压力容器制造"
                                        },
                                        {
                                            "ids": "c3333",
                                            "names": "金属包装容器制造"
                                        }]
                                },
                                {
                                    "ids": "c334",
                                    "names": "金属丝绳及其制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3340",
                                            "names": "金属丝绳及其制品制造"
                                        }]
                                },
                                {
                                    "ids": "c335",
                                    "names": "建筑、安全用金属制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3351",
                                            "names": "建筑、家具用金属配件制造"
                                        },
                                        {
                                            "ids": "c3352",
                                            "names": "建筑装饰及水暖管道零件制造"
                                        },
                                        {
                                            "ids": "c3353",
                                            "names": "安全、消防用金属制品制造"
                                        },
                                        {
                                            "ids": "c3359",
                                            "names": "其他建筑、安全用金属制品制造"
                                        }]
                                },
                                {
                                    "ids": "c336",
                                    "names": "金属表面处理及热处理加工",
                                    "childrens": [
                                        {
                                            "ids": "c3360",
                                            "names": "金属表面处理及热处理加工"
                                        }]
                                },
                                {
                                    "ids": "c337",
                                    "names": "搪瓷制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3371",
                                            "names": "生产专用搪瓷制品制造"
                                        },
                                        {
                                            "ids": "c3372",
                                            "names": "建筑装饰搪瓷制品制造"
                                        },
                                        {
                                            "ids": "c3373",
                                            "names": "搪瓷卫生洁具制造"
                                        },
                                        {
                                            "ids": "c3379",
                                            "names": "搪瓷日用品及其他搪瓷制品制造"
                                        }]
                                },
                                {
                                    "ids": "c338",
                                    "names": "金属制日用品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3381",
                                            "names": "金属制厨房用器具制造"
                                        },
                                        {
                                            "ids": "c3382",
                                            "names": "金属制餐具和器皿制造"
                                        },
                                        {
                                            "ids": "c3383",
                                            "names": "金属制卫生器具制造"
                                        },
                                        {
                                            "ids": "c3389",
                                            "names": "其他金属制日用品制造"
                                        }]
                                },
                                {
                                    "ids": "c339",
                                    "names": "其他金属制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c3391",
                                            "names": "锻件及粉末冶金制品制造"
                                        },
                                        {
                                            "ids": "c3392",
                                            "names": "交通及公共管理用金属标牌制造"
                                        },
                                        {
                                            "ids": "c3399",
                                            "names": "其他未列明金属制品制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c35",
                            "names": "专用设备制造业",
                            "childrens": [
                                {
                                    "ids": "c351",
                                    "names": "采矿、冶金、建筑专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3514",
                                            "names": "海洋工程专用设备制造"
                                        },
                                        {
                                            "ids": "c3511",
                                            "names": "矿山机械制造"
                                        },
                                        {
                                            "ids": "c3512",
                                            "names": "石油钻采专用设备制造"
                                        },
                                        {
                                            "ids": "c3513",
                                            "names": "建筑工程用机械制造"
                                        },
                                        {
                                            "ids": "c3515",
                                            "names": "建筑材料生产专用机械制造"
                                        },
                                        {
                                            "ids": "c3516",
                                            "names": "冶金专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c352",
                                    "names": "化工、木材、非金属加工专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3521",
                                            "names": "炼油、化工生产专用设备制造"
                                        },
                                        {
                                            "ids": "c3522",
                                            "names": "橡胶加工专用设备制造"
                                        },
                                        {
                                            "ids": "c3523",
                                            "names": "塑料加工专用设备制造"
                                        },
                                        {
                                            "ids": "c3524",
                                            "names": "木材加工机械制造"
                                        },
                                        {
                                            "ids": "c3525",
                                            "names": "模具制造"
                                        },
                                        {
                                            "ids": "c3529",
                                            "names": "其他非金属加工专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c353",
                                    "names": "食品、饮料、烟草及饲料生产专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3531",
                                            "names": "食品、酒、饮料及茶生产专用设备制造"
                                        },
                                        {
                                            "ids": "c3532",
                                            "names": "农副食品加工专用设备制造"
                                        },
                                        {
                                            "ids": "c3533",
                                            "names": "烟草生产专用设备制造"
                                        },
                                        {
                                            "ids": "c3534",
                                            "names": "饲料生产专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c354",
                                    "names": "印刷、制药、日化及日用品生产专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3541",
                                            "names": "制浆和造纸专用设备制造"
                                        },
                                        {
                                            "ids": "c3542",
                                            "names": "印刷专用设备制造"
                                        },
                                        {
                                            "ids": "c3543",
                                            "names": "日用化工专用设备制造"
                                        },
                                        {
                                            "ids": "c3544",
                                            "names": "制药专用设备制造"
                                        },
                                        {
                                            "ids": "c3545",
                                            "names": "照明器具生产专用设备制造"
                                        },
                                        {
                                            "ids": "c3546",
                                            "names": "玻璃、陶瓷和搪瓷制品生产专用设备制造"
                                        },
                                        {
                                            "ids": "c3549",
                                            "names": "其他日用品生产专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c355",
                                    "names": "纺织、服装和皮革加工专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3551",
                                            "names": "纺织专用设备制造"
                                        },
                                        {
                                            "ids": "c3552",
                                            "names": "皮革、毛皮及其制品加工专用设备制造"
                                        },
                                        {
                                            "ids": "c3553",
                                            "names": "缝制机械制造"
                                        },
                                        {
                                            "ids": "c3554",
                                            "names": "洗涤机械制造"
                                        }]
                                },
                                {
                                    "ids": "c356",
                                    "names": "电子和电工机械专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3561",
                                            "names": "电工机械专用设备制造"
                                        },
                                        {
                                            "ids": "c3562",
                                            "names": "电子工业专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c357",
                                    "names": "农、林、牧、渔专用机械制造",
                                    "childrens": [
                                        {
                                            "ids": "c3574",
                                            "names": "畜牧机械制造"
                                        },
                                        {
                                            "ids": "c3575",
                                            "names": "渔业机械制造"
                                        },
                                        {
                                            "ids": "c3576",
                                            "names": "农林牧渔机械配件制造"
                                        },
                                        {
                                            "ids": "c3577",
                                            "names": "棉花加工机械制造"
                                        },
                                        {
                                            "ids": "c3579",
                                            "names": "其他农、林、牧、渔业机械制造"
                                        },
                                        {
                                            "ids": "c3571",
                                            "names": "拖拉机制造"
                                        },
                                        {
                                            "ids": "c3572",
                                            "names": "机械化农业及园艺机具制造"
                                        },
                                        {
                                            "ids": "c3573",
                                            "names": "营林及木竹采伐机械制造"
                                        }]
                                },
                                {
                                    "ids": "c358",
                                    "names": "医疗仪器设备及器械制造",
                                    "childrens": [
                                        {
                                            "ids": "c3581",
                                            "names": "医疗诊断、监护及治疗设备制造"
                                        },
                                        {
                                            "ids": "c3582",
                                            "names": "口腔科用设备及器具制造"
                                        },
                                        {
                                            "ids": "c3583",
                                            "names": "医疗实验室及医用消毒设备和器具制造"
                                        },
                                        {
                                            "ids": "c3584",
                                            "names": "医疗、外科及兽医用器械制造"
                                        },
                                        {
                                            "ids": "c3585",
                                            "names": "机械治疗及病房护理设备制造"
                                        },
                                        {
                                            "ids": "c3586",
                                            "names": "假肢、人工器官及植（介）入器械制造"
                                        },
                                        {
                                            "ids": "c3589",
                                            "names": "其他医疗设备及器械制造"
                                        }]
                                },
                                {
                                    "ids": "c359",
                                    "names": "环保、社会公共服务及其他专用设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3591",
                                            "names": "环境保护专用设备制造"
                                        },
                                        {
                                            "ids": "c3592",
                                            "names": "地质勘查专用设备制造"
                                        },
                                        {
                                            "ids": "c3593",
                                            "names": "邮政专用机械及器材制造"
                                        },
                                        {
                                            "ids": "c3594",
                                            "names": "商业、饮食、服务专用设备制造"
                                        },
                                        {
                                            "ids": "c3595",
                                            "names": "社会公共安全设备及器材制造"
                                        },
                                        {
                                            "ids": "c3596",
                                            "names": "交通安全、管制及类似专用设备制造"
                                        },
                                        {
                                            "ids": "c3597",
                                            "names": "水资源专用机械制造"
                                        },
                                        {
                                            "ids": "c3599",
                                            "names": "其他专用设备制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c34",
                            "names": "通用设备制造业",
                            "childrens": [
                                {
                                    "ids": "c345",
                                    "names": "轴承、齿轮和传动部件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3451",
                                            "names": "轴承制造"
                                        },
                                        {
                                            "ids": "c3452",
                                            "names": "齿轮及齿轮减、变速箱制造"
                                        },
                                        {
                                            "ids": "c3459",
                                            "names": "其他传动部件制造"
                                        }]
                                },
                                {
                                    "ids": "c346",
                                    "names": "烘炉、风机、衡器、包装等设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3461",
                                            "names": "烘炉、熔炉及电炉制造"
                                        },
                                        {
                                            "ids": "c3462",
                                            "names": "风机、风扇制造"
                                        },
                                        {
                                            "ids": "c3463",
                                            "names": "气体、液体分离及纯净设备制造"
                                        },
                                        {
                                            "ids": "c3464",
                                            "names": "制冷、空调设备制造"
                                        },
                                        {
                                            "ids": "c3465",
                                            "names": "风动和电动工具制造"
                                        },
                                        {
                                            "ids": "c3466",
                                            "names": "喷枪及类似器具制造"
                                        },
                                        {
                                            "ids": "c3467",
                                            "names": "衡器制造"
                                        },
                                        {
                                            "ids": "c3468",
                                            "names": "包装专用设备制造"
                                        }]
                                },
                                {
                                    "ids": "c347",
                                    "names": "文化、办公用机械制造",
                                    "childrens": [
                                        {
                                            "ids": "c3471",
                                            "names": "电影机械制造"
                                        },
                                        {
                                            "ids": "c3472",
                                            "names": "幻灯及投影设备制造"
                                        },
                                        {
                                            "ids": "c3473",
                                            "names": "照相机及器材制造"
                                        },
                                        {
                                            "ids": "c3474",
                                            "names": "复印和胶印设备制造"
                                        },
                                        {
                                            "ids": "c3475",
                                            "names": "计算器及货币专用设备制造"
                                        },
                                        {
                                            "ids": "c3479",
                                            "names": "其他文化、办公用机械制造"
                                        }]
                                },
                                {
                                    "ids": "c348",
                                    "names": "通用零部件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3481",
                                            "names": "金属密封件制造"
                                        },
                                        {
                                            "ids": "c3482",
                                            "names": "紧固件制造"
                                        },
                                        {
                                            "ids": "c3483",
                                            "names": "弹簧制造"
                                        },
                                        {
                                            "ids": "c3484",
                                            "names": "机械零部件加工"
                                        },
                                        {
                                            "ids": "c3489",
                                            "names": "其他通用零部件制造"
                                        }]
                                },
                                {
                                    "ids": "c349",
                                    "names": "其他通用设备制造业",
                                    "childrens": [
                                        {
                                            "ids": "c3490",
                                            "names": "其他通用设备制造业"
                                        }]
                                },
                                {
                                    "ids": "c341",
                                    "names": "锅炉及原动设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3411",
                                            "names": "锅炉及辅助设备制造"
                                        },
                                        {
                                            "ids": "c3412",
                                            "names": "内燃机及配件制造"
                                        },
                                        {
                                            "ids": "c3413",
                                            "names": "汽轮机及辅机制造"
                                        },
                                        {
                                            "ids": "c3414",
                                            "names": "水轮机及辅机制造"
                                        },
                                        {
                                            "ids": "c3415",
                                            "names": "风能原动设备制造"
                                        },
                                        {
                                            "ids": "c3419",
                                            "names": "其他原动设备制造"
                                        }]
                                },
                                {
                                    "ids": "c342",
                                    "names": "金属加工机械制造",
                                    "childrens": [
                                        {
                                            "ids": "c3421",
                                            "names": "金属切削机床制造"
                                        },
                                        {
                                            "ids": "c3422",
                                            "names": "金属成形机床制造"
                                        },
                                        {
                                            "ids": "c3423",
                                            "names": "铸造机械制造"
                                        },
                                        {
                                            "ids": "c3424",
                                            "names": "金属切割及焊接设备制造"
                                        },
                                        {
                                            "ids": "c3425",
                                            "names": "机床附件制造"
                                        },
                                        {
                                            "ids": "c3429",
                                            "names": "其他金属加工机械制造"
                                        }]
                                },
                                {
                                    "ids": "c343",
                                    "names": "物料搬运设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3431",
                                            "names": "轻小型起重设备制造"
                                        },
                                        {
                                            "ids": "c3432",
                                            "names": "起重机制造"
                                        },
                                        {
                                            "ids": "c3433",
                                            "names": "生产专用车辆制造"
                                        },
                                        {
                                            "ids": "c3434",
                                            "names": "连续搬运设备制造"
                                        },
                                        {
                                            "ids": "c3435",
                                            "names": "电梯、自动扶梯及升降机制造"
                                        },
                                        {
                                            "ids": "c3439",
                                            "names": "其他物料搬运设备制造"
                                        }]
                                },
                                {
                                    "ids": "c344",
                                    "names": "泵、阀门、压缩机及类似机械制造",
                                    "childrens": [
                                        {
                                            "ids": "c3443",
                                            "names": "阀门和旋塞制造"
                                        },
                                        {
                                            "ids": "c3444",
                                            "names": "液压和气压动力机械及元件制造"
                                        },
                                        {
                                            "ids": "c3441",
                                            "names": "泵及真空设备制造"
                                        },
                                        {
                                            "ids": "c3442",
                                            "names": "气体压缩机械制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c36",
                            "names": "汽车制造业",
                            "childrens": [
                                {
                                    "ids": "c361",
                                    "names": "汽车整车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3610",
                                            "names": "汽车整车制造"
                                        }]
                                },
                                {
                                    "ids": "c362",
                                    "names": "改装汽车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3620",
                                            "names": "改装汽车制造"
                                        }]
                                },
                                {
                                    "ids": "c363",
                                    "names": "低速载货汽车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3630",
                                            "names": "低速载货汽车制造"
                                        }]
                                },
                                {
                                    "ids": "c364",
                                    "names": "电车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3640",
                                            "names": "电车制造"
                                        }]
                                },
                                {
                                    "ids": "c365",
                                    "names": "汽车车身、挂车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3650",
                                            "names": "汽车车身、挂车制造"
                                        }]
                                },
                                {
                                    "ids": "c366",
                                    "names": "汽车零部件及配件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3660",
                                            "names": "汽车零部件及配件制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c37",
                            "names": "铁路、船舶、航空航天和其他运输设备制造业",
                            "childrens": [
                                {
                                    "ids": "c371",
                                    "names": "铁路运输设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3711",
                                            "names": "铁路机车车辆及动车组制造"
                                        },
                                        {
                                            "ids": "c3712",
                                            "names": "窄轨机车车辆制造"
                                        },
                                        {
                                            "ids": "c3713",
                                            "names": "铁路机车车辆配件制造"
                                        },
                                        {
                                            "ids": "c3714",
                                            "names": "铁路专用设备及器材、配件制造"
                                        },
                                        {
                                            "ids": "c3719",
                                            "names": "其他铁路运输设备制造"
                                        }]
                                },
                                {
                                    "ids": "c372",
                                    "names": "城市轨道交通设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3720",
                                            "names": "城市轨道交通设备制造"
                                        }]
                                },
                                {
                                    "ids": "c373",
                                    "names": "船舶及相关装置制造",
                                    "childrens": [
                                        {
                                            "ids": "c3735",
                                            "names": "船舶改装与拆除"
                                        },
                                        {
                                            "ids": "c3739",
                                            "names": "航标器材及其他相关装置制造"
                                        },
                                        {
                                            "ids": "c3731",
                                            "names": "金属船舶制造"
                                        },
                                        {
                                            "ids": "c3732",
                                            "names": "非金属船舶制造"
                                        },
                                        {
                                            "ids": "c3733",
                                            "names": "娱乐船和运动船制造"
                                        },
                                        {
                                            "ids": "c3734",
                                            "names": "船用配套设备制造"
                                        }]
                                },
                                {
                                    "ids": "c374",
                                    "names": "航空、航天器及设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3741",
                                            "names": "飞机制造"
                                        },
                                        {
                                            "ids": "c3742",
                                            "names": "航天器制造"
                                        },
                                        {
                                            "ids": "c3743",
                                            "names": "航空、航天相关设备制造"
                                        },
                                        {
                                            "ids": "c3749",
                                            "names": "其他航空航天器制造"
                                        }]
                                },
                                {
                                    "ids": "c375",
                                    "names": "摩托车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3751",
                                            "names": "摩托车整车制造"
                                        },
                                        {
                                            "ids": "c3752",
                                            "names": "摩托车零部件及配件制造"
                                        }]
                                },
                                {
                                    "ids": "c376",
                                    "names": "自行车制造",
                                    "childrens": [
                                        {
                                            "ids": "c3761",
                                            "names": "脚踏自行车及残疾人座车制造"
                                        },
                                        {
                                            "ids": "c3762",
                                            "names": "助动自行车制造"
                                        }]
                                },
                                {
                                    "ids": "c377",
                                    "names": "非公路休闲车及零配件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3770",
                                            "names": "非公路休闲车及零配件制造"
                                        }]
                                },
                                {
                                    "ids": "c379",
                                    "names": "潜水救捞及其他未列明运输设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3791",
                                            "names": "潜水及水下救捞装备制造"
                                        },
                                        {
                                            "ids": "c3799",
                                            "names": "其他未列明运输设备制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c38",
                            "names": "电气机械和器材制造业",
                            "childrens": [
                                {
                                    "ids": "c381",
                                    "names": "电机制造",
                                    "childrens": [
                                        {
                                            "ids": "c3811",
                                            "names": "发电机及发电机组制造"
                                        },
                                        {
                                            "ids": "c3812",
                                            "names": "电动机制造"
                                        },
                                        {
                                            "ids": "c3819",
                                            "names": "微电机及其他电机制造"
                                        }]
                                },
                                {
                                    "ids": "c382",
                                    "names": "输配电及控制设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3821",
                                            "names": "变压器、整流器和电感器制造"
                                        },
                                        {
                                            "ids": "c3822",
                                            "names": "电容器及其配套设备制造"
                                        },
                                        {
                                            "ids": "c3823",
                                            "names": "配电开关控制设备制造"
                                        },
                                        {
                                            "ids": "c3824",
                                            "names": "电力电子元器件制造"
                                        },
                                        {
                                            "ids": "c3825",
                                            "names": "光伏设备及元器件制造"
                                        },
                                        {
                                            "ids": "c3829",
                                            "names": "其他输配电及控制设备制造"
                                        }]
                                },
                                {
                                    "ids": "c383",
                                    "names": "电线、电缆、光缆及电工器材制造",
                                    "childrens": [
                                        {
                                            "ids": "c3831",
                                            "names": "电线、电缆制造"
                                        },
                                        {
                                            "ids": "c3832",
                                            "names": "光纤、光缆制造"
                                        },
                                        {
                                            "ids": "c3833",
                                            "names": "绝缘制品制造"
                                        },
                                        {
                                            "ids": "c3839",
                                            "names": "其他电工器材制造"
                                        }]
                                },
                                {
                                    "ids": "c384",
                                    "names": "电池制造",
                                    "childrens": [
                                        {
                                            "ids": "c3841",
                                            "names": "锂离子电池制造"
                                        },
                                        {
                                            "ids": "c3842",
                                            "names": "镍氢电池制造"
                                        },
                                        {
                                            "ids": "c3849",
                                            "names": "其他电池制造"
                                        }]
                                },
                                {
                                    "ids": "c385",
                                    "names": "家用电力器具制造",
                                    "childrens": [
                                        {
                                            "ids": "c3851",
                                            "names": "家用制冷电器具制造"
                                        },
                                        {
                                            "ids": "c3852",
                                            "names": "家用空气调节器制造"
                                        },
                                        {
                                            "ids": "c3853",
                                            "names": "家用通风电器具制造"
                                        },
                                        {
                                            "ids": "c3854",
                                            "names": "家用厨房电器具制造"
                                        },
                                        {
                                            "ids": "c3855",
                                            "names": "家用清洁卫生电器具制造"
                                        },
                                        {
                                            "ids": "c3856",
                                            "names": "家用美容、保健电器具制造"
                                        },
                                        {
                                            "ids": "c3857",
                                            "names": "家用电力器具专用配件制造"
                                        },
                                        {
                                            "ids": "c3859",
                                            "names": "其他家用电力器具制造"
                                        }]
                                },
                                {
                                    "ids": "c386",
                                    "names": "非电力家用器具制造",
                                    "childrens": [
                                        {
                                            "ids": "c3861",
                                            "names": "燃气、太阳能及类似能源家用器具制造"
                                        },
                                        {
                                            "ids": "c3869",
                                            "names": "其他非电力家用器具制造"
                                        }]
                                },
                                {
                                    "ids": "c387",
                                    "names": "照明器具制造",
                                    "childrens": [
                                        {
                                            "ids": "c3871",
                                            "names": "电光源制造"
                                        },
                                        {
                                            "ids": "c3872",
                                            "names": "照明灯具制造"
                                        },
                                        {
                                            "ids": "c3879",
                                            "names": "灯用电器附件及其他照明器具制造"
                                        }]
                                },
                                {
                                    "ids": "c389",
                                    "names": "其他电气机械及器材制造",
                                    "childrens": [
                                        {
                                            "ids": "c3891",
                                            "names": "电气信号设备装置制造"
                                        },
                                        {
                                            "ids": "c3899",
                                            "names": "其他未列明电气机械及器材制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c39",
                            "names": "计算机、通信和其他电子设备制造业",
                            "childrens": [
                                {
                                    "ids": "c391",
                                    "names": "计算机制造",
                                    "childrens": [
                                        {
                                            "ids": "c3911",
                                            "names": "计算机整机制造"
                                        },
                                        {
                                            "ids": "c3912",
                                            "names": "计算机零部件制造"
                                        },
                                        {
                                            "ids": "c3913",
                                            "names": "计算机外围设备制造"
                                        },
                                        {
                                            "ids": "c3919",
                                            "names": "其他计算机制造"
                                        }]
                                },
                                {
                                    "ids": "c392",
                                    "names": "通信设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3921",
                                            "names": "通信系统设备制造"
                                        },
                                        {
                                            "ids": "c3922",
                                            "names": "通信终端设备制造"
                                        }]
                                },
                                {
                                    "ids": "c393",
                                    "names": "广播电视设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3939",
                                            "names": "应用电视设备及其他广播电视设备制造"
                                        },
                                        {
                                            "ids": "c3931",
                                            "names": "广播电视节目制作及发射设备制造"
                                        },
                                        {
                                            "ids": "c3932",
                                            "names": "广播电视接收设备及器材制造"
                                        }]
                                },
                                {
                                    "ids": "c394",
                                    "names": "雷达及配套设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3940",
                                            "names": "雷达及配套设备制造"
                                        }]
                                },
                                {
                                    "ids": "c395",
                                    "names": "视听设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3951",
                                            "names": "电视机制造"
                                        },
                                        {
                                            "ids": "c3952",
                                            "names": "音响设备制造"
                                        },
                                        {
                                            "ids": "c3953",
                                            "names": "影视录放设备制造"
                                        }]
                                },
                                {
                                    "ids": "c396",
                                    "names": "电子器件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3961",
                                            "names": "电子真空器件制造"
                                        },
                                        {
                                            "ids": "c3962",
                                            "names": "半导体分立器件制造"
                                        },
                                        {
                                            "ids": "c3963",
                                            "names": "集成电路制造"
                                        },
                                        {
                                            "ids": "c3969",
                                            "names": "光电子器件及其他电子器件制造"
                                        }]
                                },
                                {
                                    "ids": "c397",
                                    "names": "电子元件制造",
                                    "childrens": [
                                        {
                                            "ids": "c3971",
                                            "names": "电子元件及组件制造"
                                        },
                                        {
                                            "ids": "c3972",
                                            "names": "印制电路板制造"
                                        }]
                                },
                                {
                                    "ids": "c399",
                                    "names": "其他电子设备制造",
                                    "childrens": [
                                        {
                                            "ids": "c3990",
                                            "names": "其他电子设备制造"
                                        }]
                                }]
                        },
                        {
                            "ids": "c40",
                            "names": "仪器仪表制造业",
                            "childrens": [
                                {
                                    "ids": "c401",
                                    "names": "通用仪器仪表制造",
                                    "childrens": [
                                        {
                                            "ids": "c4011",
                                            "names": "工业自动控制系统装置制造"
                                        },
                                        {
                                            "ids": "c4012",
                                            "names": "电工仪器仪表制造"
                                        },
                                        {
                                            "ids": "c4013",
                                            "names": "绘图、计算及测量仪器制造"
                                        },
                                        {
                                            "ids": "c4014",
                                            "names": "实验分析仪器制造"
                                        },
                                        {
                                            "ids": "c4015",
                                            "names": "试验机制造"
                                        },
                                        {
                                            "ids": "c4019",
                                            "names": "供应用仪表及其他通用仪器制造"
                                        }]
                                },
                                {
                                    "ids": "c402",
                                    "names": "专用仪器仪表制造",
                                    "childrens": [
                                        {
                                            "ids": "c4021",
                                            "names": "环境监测专用仪器仪表制造"
                                        },
                                        {
                                            "ids": "c4022",
                                            "names": "运输设备及生产用计数仪表制造"
                                        },
                                        {
                                            "ids": "c4023",
                                            "names": "导航、气象及海洋专用仪器制造"
                                        },
                                        {
                                            "ids": "c4024",
                                            "names": "农林牧渔专用仪器仪表制造"
                                        },
                                        {
                                            "ids": "c4025",
                                            "names": "地质勘探和地震专用仪器制造"
                                        },
                                        {
                                            "ids": "c4026",
                                            "names": "教学专用仪器制造"
                                        },
                                        {
                                            "ids": "c4027",
                                            "names": "核子及核辐射测量仪器制造"
                                        },
                                        {
                                            "ids": "c4028",
                                            "names": "电子测量仪器制造"
                                        },
                                        {
                                            "ids": "c4029",
                                            "names": "其他专用仪器制造"
                                        }]
                                },
                                {
                                    "ids": "c403",
                                    "names": "钟表与计时仪器制造",
                                    "childrens": [
                                        {
                                            "ids": "c4030",
                                            "names": "钟表与计时仪器制造"
                                        }]
                                },
                                {
                                    "ids": "c404",
                                    "names": "光学仪器及眼镜制造",
                                    "childrens": [
                                        {
                                            "ids": "c4041",
                                            "names": "光学仪器制造"
                                        },
                                        {
                                            "ids": "c4042",
                                            "names": "眼镜制造"
                                        }]
                                },
                                {
                                    "ids": "c409",
                                    "names": "其他仪器仪表制造业",
                                    "childrens": [
                                        {
                                            "ids": "c4090",
                                            "names": "其他仪器仪表制造业"
                                        }]
                                }]
                        },
                        {
                            "ids": "c41",
                            "names": "其他制造业",
                            "childrens": [
                                {
                                    "ids": "c411",
                                    "names": "日用杂品制造",
                                    "childrens": [
                                        {
                                            "ids": "c4111",
                                            "names": "鬃毛加工、制刷及清扫工具制造"
                                        },
                                        {
                                            "ids": "c4119",
                                            "names": "其他日用杂品制造"
                                        }]
                                },
                                {
                                    "ids": "c412",
                                    "names": "煤制品制造",
                                    "childrens": [
                                        {
                                            "ids": "c4120",
                                            "names": "煤制品制造"
                                        }]
                                },
                                {
                                    "ids": "c413",
                                    "names": "核辐射加工",
                                    "childrens": [
                                        {
                                            "ids": "c4130",
                                            "names": "核辐射加工"
                                        }]
                                },
                                {
                                    "ids": "c419",
                                    "names": "其他未列明制造业",
                                    "childrens": [
                                        {
                                            "ids": "c4190",
                                            "names": "其他未列明制造业"
                                        }]
                                }]
                        },
                        {
                            "ids": "c42",
                            "names": "废弃资源综合利用业",
                            "childrens": [
                                {
                                    "ids": "c421",
                                    "names": "金属废料和碎屑加工处理",
                                    "childrens": [
                                        {
                                            "ids": "c4210",
                                            "names": "金属废料和碎屑加工处理"
                                        }]
                                },
                                {
                                    "ids": "c422",
                                    "names": "非金属废料和碎屑加工处理",
                                    "childrens": [
                                        {
                                            "ids": "c4220",
                                            "names": "非金属废料和碎屑加工处理"
                                        }]
                                }]
                        },
                        {
                            "ids": "c43",
                            "names": "金属制品、机械和设备修理业",
                            "childrens": [
                                {
                                    "ids": "c431",
                                    "names": "金属制品修理",
                                    "childrens": [
                                        {
                                            "ids": "c4310",
                                            "names": "金属制品修理"
                                        }]
                                },
                                {
                                    "ids": "c432",
                                    "names": "通用设备修理",
                                    "childrens": [
                                        {
                                            "ids": "c4320",
                                            "names": "通用设备修理"
                                        }]
                                },
                                {
                                    "ids": "c433",
                                    "names": "专用设备修理",
                                    "childrens": [
                                        {
                                            "ids": "c4330",
                                            "names": "专用设备修理"
                                        }]
                                },
                                {
                                    "ids": "c434",
                                    "names": "铁路、船舶、航空航天等运输设备修理",
                                    "childrens": [
                                        {
                                            "ids": "c4343",
                                            "names": "航空航天器修理"
                                        },
                                        {
                                            "ids": "c4349",
                                            "names": "其他运输设备修理"
                                        },
                                        {
                                            "ids": "c4342",
                                            "names": "船舶修理"
                                        },
                                        {
                                            "ids": "c4341",
                                            "names": "铁路运输设备修理"
                                        }]
                                },
                                {
                                    "ids": "c435",
                                    "names": "电气设备修理",
                                    "childrens": [
                                        {
                                            "ids": "c4350",
                                            "names": "电气设备修理"
                                        }]
                                },
                                {
                                    "ids": "c436",
                                    "names": "仪器仪表修理",
                                    "childrens": [
                                        {
                                            "ids": "c4360",
                                            "names": "仪器仪表修理"
                                        }]
                                },
                                {
                                    "ids": "c439",
                                    "names": "其他机械和设备修理业",
                                    "childrens": [
                                        {
                                            "ids": "c4390",
                                            "names": "其他机械和设备修理业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "d",
                    "names": "电力、热力、燃气及水生产和供应业",
                    "childrens": [
                        {
                            "ids": "d44",
                            "names": "电力、热力生产和供应业",
                            "childrens": [
                                {
                                    "ids": "d441",
                                    "names": "电力生产",
                                    "childrens": [
                                        {
                                            "ids": "d4411",
                                            "names": "火力发电"
                                        },
                                        {
                                            "ids": "d4412",
                                            "names": "水力发电"
                                        },
                                        {
                                            "ids": "d4413",
                                            "names": "核力发电"
                                        },
                                        {
                                            "ids": "d4414",
                                            "names": "风力发电"
                                        },
                                        {
                                            "ids": "d4415",
                                            "names": "太阳能发电"
                                        },
                                        {
                                            "ids": "d4419",
                                            "names": "其他电力生产"
                                        }]
                                },
                                {
                                    "ids": "d442",
                                    "names": "电力供应",
                                    "childrens": [
                                        {
                                            "ids": "d4420",
                                            "names": "电力供应"
                                        }]
                                },
                                {
                                    "ids": "d443",
                                    "names": "热力生产和供应",
                                    "childrens": [
                                        {
                                            "ids": "d4430",
                                            "names": "热力生产和供应"
                                        }]
                                }]
                        },
                        {
                            "ids": "d45",
                            "names": "燃气生产和供应业",
                            "childrens": [
                                {
                                    "ids": "d450",
                                    "names": "燃气生产和供应业",
                                    "childrens": [
                                        {
                                            "ids": "d4500",
                                            "names": "燃气生产和供应业"
                                        }]
                                }]
                        },
                        {
                            "ids": "d46",
                            "names": "水的生产和供应业",
                            "childrens": [
                                {
                                    "ids": "d461",
                                    "names": "自来水生产和供应",
                                    "childrens": [
                                        {
                                            "ids": "d4610",
                                            "names": "自来水生产和供应"
                                        }]
                                },
                                {
                                    "ids": "d462",
                                    "names": "污水处理及其再生利用",
                                    "childrens": [
                                        {
                                            "ids": "d4620",
                                            "names": "污水处理及其再生利用"
                                        }]
                                },
                                {
                                    "ids": "d469",
                                    "names": "其他水的处理、利用与分配",
                                    "childrens": [
                                        {
                                            "ids": "d4690",
                                            "names": "其他水的处理、利用与分配"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "e",
                    "names": "建筑业",
                    "childrens": [
                        {
                            "ids": "e50",
                            "names": "建筑装饰和其他建筑业",
                            "childrens": [
                                {
                                    "ids": "e501",
                                    "names": "建筑装饰业",
                                    "childrens": [
                                        {
                                            "ids": "e5010",
                                            "names": "建筑装饰业"
                                        }]
                                },
                                {
                                    "ids": "e502",
                                    "names": "工程准备活动",
                                    "childrens": [
                                        {
                                            "ids": "e5021",
                                            "names": "建筑物拆除活动"
                                        },
                                        {
                                            "ids": "e5029",
                                            "names": "其他工程准备活动"
                                        }]
                                },
                                {
                                    "ids": "e503",
                                    "names": "提供施工设备服务",
                                    "childrens": [
                                        {
                                            "ids": "e5030",
                                            "names": "提供施工设备服务"
                                        }]
                                },
                                {
                                    "ids": "e509",
                                    "names": "其他未列明建筑业",
                                    "childrens": [
                                        {
                                            "ids": "e5090",
                                            "names": "其他未列明建筑业"
                                        }]
                                }]
                        },
                        {
                            "ids": "e47",
                            "names": "房屋建筑业",
                            "childrens": [
                                {
                                    "ids": "e470",
                                    "names": "房屋建筑业",
                                    "childrens": [
                                        {
                                            "ids": "e4700",
                                            "names": "房屋建筑业"
                                        }]
                                }]
                        },
                        {
                            "ids": "e48",
                            "names": "土木工程建筑业",
                            "childrens": [
                                {
                                    "ids": "e481",
                                    "names": "铁路、道路、隧道和桥梁工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4811",
                                            "names": "铁路工程建筑"
                                        },
                                        {
                                            "ids": "e4812",
                                            "names": "公路工程建筑"
                                        },
                                        {
                                            "ids": "e4813",
                                            "names": "市政道路工程建筑"
                                        },
                                        {
                                            "ids": "e4819",
                                            "names": "其他道路、隧道和桥梁工程建筑"
                                        }]
                                },
                                {
                                    "ids": "e482",
                                    "names": "水利和内河港口工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4821",
                                            "names": "水源及供水设施工程建筑"
                                        },
                                        {
                                            "ids": "e4822",
                                            "names": "河湖治理及防洪设施工程建筑"
                                        },
                                        {
                                            "ids": "e4823",
                                            "names": "港口及航运设施工程建筑"
                                        }]
                                },
                                {
                                    "ids": "e483",
                                    "names": "海洋工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4830",
                                            "names": "海洋工程建筑"
                                        }]
                                },
                                {
                                    "ids": "e484",
                                    "names": "工矿工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4840",
                                            "names": "工矿工程建筑"
                                        }]
                                },
                                {
                                    "ids": "e485",
                                    "names": "架线和管道工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4851",
                                            "names": "架线及设备工程建筑"
                                        },
                                        {
                                            "ids": "e4852",
                                            "names": "管道工程建筑"
                                        }]
                                },
                                {
                                    "ids": "e489",
                                    "names": "其他土木工程建筑",
                                    "childrens": [
                                        {
                                            "ids": "e4890",
                                            "names": "其他土木工程建筑"
                                        }]
                                }]
                        },
                        {
                            "ids": "e49",
                            "names": "建筑安装业",
                            "childrens": [
                                {
                                    "ids": "e491",
                                    "names": "电气安装",
                                    "childrens": [
                                        {
                                            "ids": "e4910",
                                            "names": "电气安装"
                                        }]
                                },
                                {
                                    "ids": "e492",
                                    "names": "管道和设备安装",
                                    "childrens": [
                                        {
                                            "ids": "e4920",
                                            "names": "管道和设备安装"
                                        }]
                                },
                                {
                                    "ids": "e499",
                                    "names": "其他建筑安装业",
                                    "childrens": [
                                        {
                                            "ids": "e4990",
                                            "names": "其他建筑安装业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "f",
                    "names": "批发和零售业",
                    "childrens": [
                        {
                            "ids": "f52",
                            "names": "零售业",
                            "childrens": [
                                {
                                    "ids": "f528",
                                    "names": "五金、家具及室内装饰材料专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5281",
                                            "names": "五金零售"
                                        },
                                        {
                                            "ids": "f5282",
                                            "names": "灯具零售"
                                        },
                                        {
                                            "ids": "f5283",
                                            "names": "家具零售"
                                        },
                                        {
                                            "ids": "f5284",
                                            "names": "涂料零售"
                                        },
                                        {
                                            "ids": "f5285",
                                            "names": "卫生洁具零售"
                                        },
                                        {
                                            "ids": "f5286",
                                            "names": "木质装饰材料零售"
                                        },
                                        {
                                            "ids": "f5287",
                                            "names": "陶瓷、石材装饰材料零售"
                                        },
                                        {
                                            "ids": "f5289",
                                            "names": "其他室内装饰材料零售"
                                        }]
                                },
                                {
                                    "ids": "f529",
                                    "names": "货摊、无店铺及其他零售业",
                                    "childrens": [
                                        {
                                            "ids": "f5291",
                                            "names": "货摊食品零售"
                                        },
                                        {
                                            "ids": "f5292",
                                            "names": "货摊纺织、服装及鞋零售"
                                        },
                                        {
                                            "ids": "f5293",
                                            "names": "货摊日用品零售"
                                        },
                                        {
                                            "ids": "f5294",
                                            "names": "互联网零售"
                                        },
                                        {
                                            "ids": "f5295",
                                            "names": "邮购及电视、电话零售"
                                        },
                                        {
                                            "ids": "f5296",
                                            "names": "旧货零售"
                                        },
                                        {
                                            "ids": "f5297",
                                            "names": "生活用燃料零售"
                                        },
                                        {
                                            "ids": "f5299",
                                            "names": "其他未列明零售业"
                                        }]
                                },
                                {
                                    "ids": "f521",
                                    "names": "综合零售",
                                    "childrens": [
                                        {
                                            "ids": "f5211",
                                            "names": "百货零售"
                                        },
                                        {
                                            "ids": "f5212",
                                            "names": "超级市场零售"
                                        },
                                        {
                                            "ids": "f5219",
                                            "names": "其他综合零售"
                                        }]
                                },
                                {
                                    "ids": "f522",
                                    "names": "食品、饮料及烟草制品专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5221",
                                            "names": "粮油零售"
                                        },
                                        {
                                            "ids": "f5222",
                                            "names": "糕点、面包零售"
                                        },
                                        {
                                            "ids": "f5223",
                                            "names": "果品、蔬菜零售"
                                        },
                                        {
                                            "ids": "f5224",
                                            "names": "肉、禽、蛋、奶及水产品零售"
                                        },
                                        {
                                            "ids": "f5225",
                                            "names": "营养和保健品零售"
                                        },
                                        {
                                            "ids": "f5226",
                                            "names": "酒、饮料及茶叶零售"
                                        },
                                        {
                                            "ids": "f5227",
                                            "names": "烟草制品零售"
                                        },
                                        {
                                            "ids": "f5229",
                                            "names": "其他食品零售"
                                        }]
                                },
                                {
                                    "ids": "f523",
                                    "names": "纺织、服装及日用品专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5231",
                                            "names": "纺织品及针织品零售"
                                        },
                                        {
                                            "ids": "f5232",
                                            "names": "服装零售"
                                        },
                                        {
                                            "ids": "f5233",
                                            "names": "鞋帽零售"
                                        },
                                        {
                                            "ids": "f5234",
                                            "names": "化妆品及卫生用品零售"
                                        },
                                        {
                                            "ids": "f5235",
                                            "names": "钟表、眼镜零售"
                                        },
                                        {
                                            "ids": "f5236",
                                            "names": "箱、包零售"
                                        },
                                        {
                                            "ids": "f5237",
                                            "names": "厨房用具及日用杂品零售"
                                        },
                                        {
                                            "ids": "f5238",
                                            "names": "自行车零售"
                                        },
                                        {
                                            "ids": "f5239",
                                            "names": "其他日用品零售"
                                        }]
                                },
                                {
                                    "ids": "f524",
                                    "names": "文化、体育用品及器材专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5242",
                                            "names": "体育用品及器材零售"
                                        },
                                        {
                                            "ids": "f5243",
                                            "names": "图书、报刊零售"
                                        },
                                        {
                                            "ids": "f5244",
                                            "names": "音像制品及电子出版物零售"
                                        },
                                        {
                                            "ids": "f5245",
                                            "names": "珠宝首饰零售"
                                        },
                                        {
                                            "ids": "f5246",
                                            "names": "工艺美术品及收藏品零售"
                                        },
                                        {
                                            "ids": "f5247",
                                            "names": "乐器零售"
                                        },
                                        {
                                            "ids": "f5248",
                                            "names": "照相器材零售"
                                        },
                                        {
                                            "ids": "f5249",
                                            "names": "其他文化用品零售"
                                        },
                                        {
                                            "ids": "f5241",
                                            "names": "文具用品零售"
                                        }]
                                },
                                {
                                    "ids": "f525",
                                    "names": "医药及医疗器材专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5251",
                                            "names": "药品零售"
                                        },
                                        {
                                            "ids": "f5252",
                                            "names": "医疗用品及器材零售"
                                        }]
                                },
                                {
                                    "ids": "f526",
                                    "names": "汽车、摩托车、燃料及零配件专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5261",
                                            "names": "汽车零售"
                                        },
                                        {
                                            "ids": "f5262",
                                            "names": "汽车零配件零售"
                                        },
                                        {
                                            "ids": "f5263",
                                            "names": "摩托车及零配件零售"
                                        },
                                        {
                                            "ids": "f5264",
                                            "names": "机动车燃料零售"
                                        }]
                                },
                                {
                                    "ids": "f527",
                                    "names": "家用电器及电子产品专门零售",
                                    "childrens": [
                                        {
                                            "ids": "f5271",
                                            "names": "家用视听设备零售"
                                        },
                                        {
                                            "ids": "f5272",
                                            "names": "日用家电设备零售"
                                        },
                                        {
                                            "ids": "f5273",
                                            "names": "计算机、软件及辅助设备零售"
                                        },
                                        {
                                            "ids": "f5274",
                                            "names": "通信设备零售"
                                        },
                                        {
                                            "ids": "f5279",
                                            "names": "其他电子产品零售"
                                        }]
                                }]
                        },
                        {
                            "ids": "f51",
                            "names": "批发业",
                            "childrens": [
                                {
                                    "ids": "f517",
                                    "names": "机械设备、五金产品及电子产品批发",
                                    "childrens": [
                                        {
                                            "ids": "f5171",
                                            "names": "农业机械批发"
                                        },
                                        {
                                            "ids": "f5172",
                                            "names": "汽车批发"
                                        },
                                        {
                                            "ids": "f5173",
                                            "names": "汽车零配件批发"
                                        },
                                        {
                                            "ids": "f5174",
                                            "names": "摩托车及零配件批发"
                                        },
                                        {
                                            "ids": "f5175",
                                            "names": "五金产品批发"
                                        },
                                        {
                                            "ids": "f5176",
                                            "names": "电气设备批发"
                                        },
                                        {
                                            "ids": "f5177",
                                            "names": "计算机、软件及辅助设备批发"
                                        },
                                        {
                                            "ids": "f5178",
                                            "names": "通讯及广播电视设备批发"
                                        },
                                        {
                                            "ids": "f5179",
                                            "names": "其他机械设备及电子产品批发"
                                        }]
                                },
                                {
                                    "ids": "f518",
                                    "names": "贸易经纪与代理",
                                    "childrens": [
                                        {
                                            "ids": "f5181",
                                            "names": "贸易代理"
                                        },
                                        {
                                            "ids": "f5182",
                                            "names": "拍卖"
                                        },
                                        {
                                            "ids": "f5189",
                                            "names": "其他贸易经纪与代理"
                                        }]
                                },
                                {
                                    "ids": "f519",
                                    "names": "其他批发业",
                                    "childrens": [
                                        {
                                            "ids": "f5191",
                                            "names": "再生物资回收与批发"
                                        },
                                        {
                                            "ids": "f5199",
                                            "names": "其他未列明批发业"
                                        }]
                                },
                                {
                                    "ids": "f511",
                                    "names": "农、林、牧产品批发",
                                    "childrens": [
                                        {
                                            "ids": "f5111",
                                            "names": "谷物、豆及薯类批发"
                                        },
                                        {
                                            "ids": "f5112",
                                            "names": "种子批发"
                                        },
                                        {
                                            "ids": "f5113",
                                            "names": "饲料批发"
                                        },
                                        {
                                            "ids": "f5114",
                                            "names": "棉、麻批发"
                                        },
                                        {
                                            "ids": "f5115",
                                            "names": "林业产品批发"
                                        },
                                        {
                                            "ids": "f5116",
                                            "names": "牲畜批发"
                                        },
                                        {
                                            "ids": "f5119",
                                            "names": "其他农牧产品批发"
                                        }]
                                },
                                {
                                    "ids": "f512",
                                    "names": "食品、饮料及烟草制品批发",
                                    "childrens": [
                                        {
                                            "ids": "f5121",
                                            "names": "米、面制品及食用油批发"
                                        },
                                        {
                                            "ids": "f5122",
                                            "names": "糕点、糖果及糖批发"
                                        },
                                        {
                                            "ids": "f5123",
                                            "names": "果品、蔬菜批发"
                                        },
                                        {
                                            "ids": "f5124",
                                            "names": "肉、禽、蛋、奶及水产品批发"
                                        },
                                        {
                                            "ids": "f5125",
                                            "names": "盐及调味品批发"
                                        },
                                        {
                                            "ids": "f5126",
                                            "names": "营养和保健品批发"
                                        },
                                        {
                                            "ids": "f5127",
                                            "names": "酒、饮料及茶叶批发"
                                        },
                                        {
                                            "ids": "f5128",
                                            "names": "烟草制品批发"
                                        },
                                        {
                                            "ids": "f5129",
                                            "names": "其他食品批发"
                                        }]
                                },
                                {
                                    "ids": "f513",
                                    "names": "纺织、服装及家庭用品批发",
                                    "childrens": [
                                        {
                                            "ids": "f5131",
                                            "names": "纺织品、针织品及原料批发"
                                        },
                                        {
                                            "ids": "f5132",
                                            "names": "服装批发"
                                        },
                                        {
                                            "ids": "f5133",
                                            "names": "鞋帽批发"
                                        },
                                        {
                                            "ids": "f5134",
                                            "names": "化妆品及卫生用品批发"
                                        },
                                        {
                                            "ids": "f5135",
                                            "names": "厨房、卫生间用具及日用杂货批发"
                                        },
                                        {
                                            "ids": "f5136",
                                            "names": "灯具、装饰物品批发"
                                        },
                                        {
                                            "ids": "f5137",
                                            "names": "家用电器批发"
                                        },
                                        {
                                            "ids": "f5139",
                                            "names": "其他家庭用品批发"
                                        }]
                                },
                                {
                                    "ids": "f514",
                                    "names": "文化、体育用品及器材批发",
                                    "childrens": [
                                        {
                                            "ids": "f5141",
                                            "names": "文具用品批发"
                                        },
                                        {
                                            "ids": "f5142",
                                            "names": "体育用品及器材批发"
                                        },
                                        {
                                            "ids": "f5143",
                                            "names": "图书批发"
                                        },
                                        {
                                            "ids": "f5144",
                                            "names": "报刊批发"
                                        },
                                        {
                                            "ids": "f5145",
                                            "names": "音像制品及电子出版物批发"
                                        },
                                        {
                                            "ids": "f5146",
                                            "names": "首饰、工艺品及收藏品批发"
                                        },
                                        {
                                            "ids": "f5149",
                                            "names": "其他文化用品批发"
                                        }]
                                },
                                {
                                    "ids": "f515",
                                    "names": "医药及医疗器材批发",
                                    "childrens": [
                                        {
                                            "ids": "f5151",
                                            "names": "西药批发"
                                        },
                                        {
                                            "ids": "f5152",
                                            "names": "中药批发"
                                        },
                                        {
                                            "ids": "f5153",
                                            "names": "医疗用品及器材批发"
                                        }]
                                },
                                {
                                    "ids": "f516",
                                    "names": "矿产品、建材及化工产品批发",
                                    "childrens": [
                                        {
                                            "ids": "f5161",
                                            "names": "煤炭及制品批发"
                                        },
                                        {
                                            "ids": "f5162",
                                            "names": "石油及制品批发"
                                        },
                                        {
                                            "ids": "f5163",
                                            "names": "非金属矿及制品批发"
                                        },
                                        {
                                            "ids": "f5164",
                                            "names": "金属及金属矿批发"
                                        },
                                        {
                                            "ids": "f5165",
                                            "names": "建材批发"
                                        },
                                        {
                                            "ids": "f5166",
                                            "names": "化肥批发"
                                        },
                                        {
                                            "ids": "f5167",
                                            "names": "农药批发"
                                        },
                                        {
                                            "ids": "f5168",
                                            "names": "农用薄膜批发"
                                        },
                                        {
                                            "ids": "f5169",
                                            "names": "其他化工产品批发"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "g",
                    "names": "交通运输、仓储和邮政业",
                    "childrens": [
                        {
                            "ids": "g55",
                            "names": "水上运输业",
                            "childrens": [
                                {
                                    "ids": "g551",
                                    "names": "水上旅客运输",
                                    "childrens": [
                                        {
                                            "ids": "g5513",
                                            "names": "客运轮渡运输"
                                        },
                                        {
                                            "ids": "g5511",
                                            "names": "海洋旅客运输"
                                        },
                                        {
                                            "ids": "g5512",
                                            "names": "内河旅客运输"
                                        }]
                                },
                                {
                                    "ids": "g552",
                                    "names": "水上货物运输",
                                    "childrens": [
                                        {
                                            "ids": "g5521",
                                            "names": "远洋货物运输"
                                        },
                                        {
                                            "ids": "g5522",
                                            "names": "沿海货物运输"
                                        },
                                        {
                                            "ids": "g5523",
                                            "names": "内河货物运输"
                                        }]
                                },
                                {
                                    "ids": "g553",
                                    "names": "水上运输辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "g5531",
                                            "names": "客运港口"
                                        },
                                        {
                                            "ids": "g5532",
                                            "names": "货运港口"
                                        },
                                        {
                                            "ids": "g5539",
                                            "names": "其他水上运输辅助活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "g53",
                            "names": "铁路运输业",
                            "childrens": [
                                {
                                    "ids": "g531",
                                    "names": "铁路旅客运输",
                                    "childrens": [
                                        {
                                            "ids": "g5310",
                                            "names": "铁路旅客运输"
                                        }]
                                },
                                {
                                    "ids": "g532",
                                    "names": "铁路货物运输",
                                    "childrens": [
                                        {
                                            "ids": "g5320",
                                            "names": "铁路货物运输"
                                        }]
                                },
                                {
                                    "ids": "g533",
                                    "names": "铁路运输辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "g5331",
                                            "names": "客运火车站"
                                        },
                                        {
                                            "ids": "g5332",
                                            "names": "货运火车站"
                                        },
                                        {
                                            "ids": "g5339",
                                            "names": "其他铁路运输辅助活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "g54",
                            "names": "道路运输业",
                            "childrens": [
                                {
                                    "ids": "g542",
                                    "names": "公路旅客运输",
                                    "childrens": [
                                        {
                                            "ids": "g5420",
                                            "names": "公路旅客运输"
                                        }]
                                },
                                {
                                    "ids": "g543",
                                    "names": "道路货物运输",
                                    "childrens": [
                                        {
                                            "ids": "g5430",
                                            "names": "道路货物运输"
                                        }]
                                },
                                {
                                    "ids": "g544",
                                    "names": "道路运输辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "g5441",
                                            "names": "客运汽车站"
                                        },
                                        {
                                            "ids": "g5442",
                                            "names": "公路管理与养护"
                                        },
                                        {
                                            "ids": "g5449",
                                            "names": "其他道路运输辅助活动"
                                        }]
                                },
                                {
                                    "ids": "g541",
                                    "names": "城市公共交通运输",
                                    "childrens": [
                                        {
                                            "ids": "g5413",
                                            "names": "出租车客运"
                                        },
                                        {
                                            "ids": "g5419",
                                            "names": "其他城市公共交通运输"
                                        },
                                        {
                                            "ids": "g5411",
                                            "names": "公共电汽车客运"
                                        },
                                        {
                                            "ids": "g5412",
                                            "names": "城市轨道交通"
                                        }]
                                }]
                        },
                        {
                            "ids": "g56",
                            "names": "航空运输业",
                            "childrens": [
                                {
                                    "ids": "g561",
                                    "names": "航空客货运输",
                                    "childrens": [
                                        {
                                            "ids": "g5611",
                                            "names": "航空旅客运输"
                                        },
                                        {
                                            "ids": "g5612",
                                            "names": "航空货物运输"
                                        }]
                                },
                                {
                                    "ids": "g562",
                                    "names": "通用航空服务",
                                    "childrens": [
                                        {
                                            "ids": "g5620",
                                            "names": "通用航空服务"
                                        }]
                                },
                                {
                                    "ids": "g563",
                                    "names": "航空运输辅助活动",
                                    "childrens": [
                                        {
                                            "ids": "g5631",
                                            "names": "机场"
                                        },
                                        {
                                            "ids": "g5632",
                                            "names": "空中交通管理"
                                        },
                                        {
                                            "ids": "g5639",
                                            "names": "其他航空运输辅助活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "g57",
                            "names": "管道运输业",
                            "childrens": [
                                {
                                    "ids": "g570",
                                    "names": "管道运输业",
                                    "childrens": [
                                        {
                                            "ids": "g5700",
                                            "names": "管道运输业"
                                        }]
                                }]
                        },
                        {
                            "ids": "g58",
                            "names": "装卸搬运和运输代理业",
                            "childrens": [
                                {
                                    "ids": "g581",
                                    "names": "装卸搬运",
                                    "childrens": [
                                        {
                                            "ids": "g5810",
                                            "names": "装卸搬运"
                                        }]
                                },
                                {
                                    "ids": "g582",
                                    "names": "运输代理业",
                                    "childrens": [
                                        {
                                            "ids": "g5821",
                                            "names": "货物运输代理"
                                        },
                                        {
                                            "ids": "g5822",
                                            "names": "旅客票务代理"
                                        },
                                        {
                                            "ids": "g5829",
                                            "names": "其他运输代理业"
                                        }]
                                }]
                        },
                        {
                            "ids": "g59",
                            "names": "仓储业",
                            "childrens": [
                                {
                                    "ids": "g591",
                                    "names": "谷物、棉花等农产品仓储",
                                    "childrens": [
                                        {
                                            "ids": "g5911",
                                            "names": "谷物仓储"
                                        },
                                        {
                                            "ids": "g5912",
                                            "names": "棉花仓储"
                                        },
                                        {
                                            "ids": "g5919",
                                            "names": "其他农产品仓储"
                                        }]
                                },
                                {
                                    "ids": "g599",
                                    "names": "其他仓储业",
                                    "childrens": [
                                        {
                                            "ids": "g5990",
                                            "names": "其他仓储业"
                                        }]
                                }]
                        },
                        {
                            "ids": "g60",
                            "names": "邮政业",
                            "childrens": [
                                {
                                    "ids": "g601",
                                    "names": "邮政基本服务",
                                    "childrens": [
                                        {
                                            "ids": "g6010",
                                            "names": "邮政基本服务"
                                        }]
                                },
                                {
                                    "ids": "g602",
                                    "names": "快递服务",
                                    "childrens": [
                                        {
                                            "ids": "g6020",
                                            "names": "快递服务"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "h",
                    "names": "住宿和餐饮业",
                    "childrens": [
                        {
                            "ids": "h61",
                            "names": "住宿业",
                            "childrens": [
                                {
                                    "ids": "h611",
                                    "names": "旅游饭店",
                                    "childrens": [
                                        {
                                            "ids": "h6110",
                                            "names": "旅游饭店"
                                        }]
                                },
                                {
                                    "ids": "h612",
                                    "names": "一般旅馆",
                                    "childrens": [
                                        {
                                            "ids": "h6120",
                                            "names": "一般旅馆"
                                        }]
                                },
                                {
                                    "ids": "h619",
                                    "names": "其他住宿业",
                                    "childrens": [
                                        {
                                            "ids": "h6190",
                                            "names": "其他住宿业"
                                        }]
                                }]
                        },
                        {
                            "ids": "h62",
                            "names": "餐饮业",
                            "childrens": [
                                {
                                    "ids": "h621",
                                    "names": "正餐服务",
                                    "childrens": [
                                        {
                                            "ids": "h6210",
                                            "names": "正餐服务"
                                        }]
                                },
                                {
                                    "ids": "h622",
                                    "names": "快餐服务",
                                    "childrens": [
                                        {
                                            "ids": "h6220",
                                            "names": "快餐服务"
                                        }]
                                },
                                {
                                    "ids": "h623",
                                    "names": "饮料及冷饮服务",
                                    "childrens": [
                                        {
                                            "ids": "h6231",
                                            "names": "茶馆服务"
                                        },
                                        {
                                            "ids": "h6232",
                                            "names": "咖啡馆服务"
                                        },
                                        {
                                            "ids": "h6233",
                                            "names": "酒吧服务"
                                        },
                                        {
                                            "ids": "h6239",
                                            "names": "其他饮料及冷饮服务"
                                        }]
                                },
                                {
                                    "ids": "h629",
                                    "names": "其他餐饮业",
                                    "childrens": [
                                        {
                                            "ids": "h6291",
                                            "names": "小吃服务"
                                        },
                                        {
                                            "ids": "h6292",
                                            "names": "餐饮配送服务"
                                        },
                                        {
                                            "ids": "h6299",
                                            "names": "其他未列明餐饮业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "i",
                    "names": "信息传输、软件和信息技术服务业",
                    "childrens": [
                        {
                            "ids": "i63",
                            "names": "电信、广播电视和卫星传输服务",
                            "childrens": [
                                {
                                    "ids": "i631",
                                    "names": "电信",
                                    "childrens": [
                                        {
                                            "ids": "i6311",
                                            "names": "固定电信服务"
                                        },
                                        {
                                            "ids": "i6312",
                                            "names": "移动电信服务"
                                        },
                                        {
                                            "ids": "i6319",
                                            "names": "其他电信服务"
                                        }]
                                },
                                {
                                    "ids": "i632",
                                    "names": "广播电视传输服务",
                                    "childrens": [
                                        {
                                            "ids": "i6321",
                                            "names": "有线广播电视传输服务"
                                        },
                                        {
                                            "ids": "i6322",
                                            "names": "无线广播电视传输服务"
                                        }]
                                },
                                {
                                    "ids": "i633",
                                    "names": "卫星传输服务",
                                    "childrens": [
                                        {
                                            "ids": "i6330",
                                            "names": "卫星传输服务"
                                        }]
                                }]
                        },
                        {
                            "ids": "i64",
                            "names": "互联网和相关服务",
                            "childrens": [
                                {
                                    "ids": "i641",
                                    "names": "互联网接入及相关服务",
                                    "childrens": [
                                        {
                                            "ids": "i6410",
                                            "names": "互联网接入及相关服务"
                                        }]
                                },
                                {
                                    "ids": "i642",
                                    "names": "互联网信息服务",
                                    "childrens": [
                                        {
                                            "ids": "i6420",
                                            "names": "互联网信息服务"
                                        }]
                                },
                                {
                                    "ids": "i649",
                                    "names": "其他互联网服务",
                                    "childrens": [
                                        {
                                            "ids": "i6490",
                                            "names": "其他互联网服务"
                                        }]
                                }]
                        },
                        {
                            "ids": "i65",
                            "names": "软件和信息技术服务业",
                            "childrens": [
                                {
                                    "ids": "i654",
                                    "names": "数据处理和存储服务",
                                    "childrens": [
                                        {
                                            "ids": "i6540",
                                            "names": "数据处理和存储服务"
                                        }]
                                },
                                {
                                    "ids": "i655",
                                    "names": "集成电路设计",
                                    "childrens": [
                                        {
                                            "ids": "i6550",
                                            "names": "集成电路设计"
                                        }]
                                },
                                {
                                    "ids": "i659",
                                    "names": "其他信息技术服务业",
                                    "childrens": [
                                        {
                                            "ids": "i6591",
                                            "names": "数字内容服务"
                                        },
                                        {
                                            "ids": "i6592",
                                            "names": "呼叫中心"
                                        },
                                        {
                                            "ids": "i6599",
                                            "names": "其他未列明信息技术服务业"
                                        }]
                                },
                                {
                                    "ids": "i651",
                                    "names": "软件开发",
                                    "childrens": [
                                        {
                                            "ids": "i6510",
                                            "names": "软件开发"
                                        }]
                                },
                                {
                                    "ids": "i652",
                                    "names": "信息系统集成服务",
                                    "childrens": [
                                        {
                                            "ids": "i6520",
                                            "names": "信息系统集成服务"
                                        }]
                                },
                                {
                                    "ids": "i653",
                                    "names": "信息技术咨询服务",
                                    "childrens": [
                                        {
                                            "ids": "i6530",
                                            "names": "信息技术咨询服务"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "j",
                    "names": "金融业",
                    "childrens": [
                        {
                            "ids": "j68",
                            "names": "保险业",
                            "childrens": [
                                {
                                    "ids": "j681",
                                    "names": "人身保险",
                                    "childrens": [
                                        {
                                            "ids": "j6811",
                                            "names": "人寿保险"
                                        },
                                        {
                                            "ids": "j6812",
                                            "names": "健康和意外保险"
                                        }]
                                },
                                {
                                    "ids": "j682",
                                    "names": "财产保险",
                                    "childrens": [
                                        {
                                            "ids": "j6820",
                                            "names": "财产保险"
                                        }]
                                },
                                {
                                    "ids": "j683",
                                    "names": "再保险",
                                    "childrens": [
                                        {
                                            "ids": "j6830",
                                            "names": "再保险"
                                        }]
                                },
                                {
                                    "ids": "j684",
                                    "names": "养老金",
                                    "childrens": [
                                        {
                                            "ids": "j6840",
                                            "names": "养老金"
                                        }]
                                },
                                {
                                    "ids": "j685",
                                    "names": "保险经纪与代理服务",
                                    "childrens": [
                                        {
                                            "ids": "j6850",
                                            "names": "保险经纪与代理服务"
                                        }]
                                },
                                {
                                    "ids": "j686",
                                    "names": "保险监管服务",
                                    "childrens": [
                                        {
                                            "ids": "j6860",
                                            "names": "保险监管服务"
                                        }]
                                },
                                {
                                    "ids": "j689",
                                    "names": "其他保险活动",
                                    "childrens": [
                                        {
                                            "ids": "j6891",
                                            "names": "风险和损失评估"
                                        },
                                        {
                                            "ids": "j6899",
                                            "names": "其他未列明保险活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "j66",
                            "names": "货币金融服务",
                            "childrens": [
                                {
                                    "ids": "j661",
                                    "names": "中央银行服务",
                                    "childrens": [
                                        {
                                            "ids": "j6610",
                                            "names": "中央银行服务"
                                        }]
                                },
                                {
                                    "ids": "j662",
                                    "names": "货币银行服务",
                                    "childrens": [
                                        {
                                            "ids": "j6620",
                                            "names": "货币银行服务"
                                        }]
                                },
                                {
                                    "ids": "j663",
                                    "names": "非货币银行服务",
                                    "childrens": [
                                        {
                                            "ids": "j6631",
                                            "names": "金融租赁服务"
                                        },
                                        {
                                            "ids": "j6632",
                                            "names": "财务公司"
                                        },
                                        {
                                            "ids": "j6633",
                                            "names": "典当"
                                        },
                                        {
                                            "ids": "j6639",
                                            "names": "其他非货币银行服务"
                                        }]
                                },
                                {
                                    "ids": "j664",
                                    "names": "银行监管服务",
                                    "childrens": [
                                        {
                                            "ids": "j6640",
                                            "names": "银行监管服务"
                                        }]
                                }]
                        },
                        {
                            "ids": "j67",
                            "names": "资本市场服务",
                            "childrens": [
                                {
                                    "ids": "j672",
                                    "names": "期货市场服务",
                                    "childrens": [
                                        {
                                            "ids": "j6721",
                                            "names": "期货市场管理服务"
                                        },
                                        {
                                            "ids": "j6729",
                                            "names": "其他期货市场服务"
                                        }]
                                },
                                {
                                    "ids": "j673",
                                    "names": "证券期货监管服务",
                                    "childrens": [
                                        {
                                            "ids": "j6730",
                                            "names": "证券期货监管服务"
                                        }]
                                },
                                {
                                    "ids": "j674",
                                    "names": "资本投资服务",
                                    "childrens": [
                                        {
                                            "ids": "j6740",
                                            "names": "资本投资服务"
                                        }]
                                },
                                {
                                    "ids": "j679",
                                    "names": "其他资本市场服务",
                                    "childrens": [
                                        {
                                            "ids": "j6790",
                                            "names": "其他资本市场服务"
                                        }]
                                },
                                {
                                    "ids": "j671",
                                    "names": "证券市场服务",
                                    "childrens": [
                                        {
                                            "ids": "j6711",
                                            "names": "证券市场管理服务"
                                        },
                                        {
                                            "ids": "j6712",
                                            "names": "证券经纪交易服务"
                                        },
                                        {
                                            "ids": "j6713",
                                            "names": "基金管理服务"
                                        }]
                                }]
                        },
                        {
                            "ids": "j69",
                            "names": "其他金融业",
                            "childrens": [
                                {
                                    "ids": "j691",
                                    "names": "金融信托与管理服务",
                                    "childrens": [
                                        {
                                            "ids": "j6910",
                                            "names": "金融信托与管理服务"
                                        }]
                                },
                                {
                                    "ids": "j692",
                                    "names": "控股公司服务",
                                    "childrens": [
                                        {
                                            "ids": "j6920",
                                            "names": "控股公司服务"
                                        }]
                                },
                                {
                                    "ids": "j693",
                                    "names": "非金融机构支付服务",
                                    "childrens": [
                                        {
                                            "ids": "j6930",
                                            "names": "非金融机构支付服务"
                                        }]
                                },
                                {
                                    "ids": "j694",
                                    "names": "金融信息服务",
                                    "childrens": [
                                        {
                                            "ids": "j6940",
                                            "names": "金融信息服务"
                                        }]
                                },
                                {
                                    "ids": "j699",
                                    "names": "其他未列明金融业",
                                    "childrens": [
                                        {
                                            "ids": "j6990",
                                            "names": "其他未列明金融业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "k",
                    "names": "房地产业",
                    "childrens": [
                        {
                            "ids": "k70",
                            "names": "房地产业",
                            "childrens": [
                                {
                                    "ids": "k701",
                                    "names": "房地产开发经营",
                                    "childrens": [
                                        {
                                            "ids": "k7010",
                                            "names": "房地产开发经营"
                                        }]
                                },
                                {
                                    "ids": "k702",
                                    "names": "物业管理",
                                    "childrens": [
                                        {
                                            "ids": "k7020",
                                            "names": "物业管理"
                                        }]
                                },
                                {
                                    "ids": "k703",
                                    "names": "房地产中介服务",
                                    "childrens": [
                                        {
                                            "ids": "k7030",
                                            "names": "房地产中介服务"
                                        }]
                                },
                                {
                                    "ids": "k704",
                                    "names": "自有房地产经营活动",
                                    "childrens": [
                                        {
                                            "ids": "k7040",
                                            "names": "自有房地产经营活动"
                                        }]
                                },
                                {
                                    "ids": "k709",
                                    "names": "其他房地产业",
                                    "childrens": [
                                        {
                                            "ids": "k7090",
                                            "names": "其他房地产业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "l",
                    "names": "租赁和商务服务业",
                    "childrens": [
                        {
                            "ids": "l71",
                            "names": "租赁业",
                            "childrens": [
                                {
                                    "ids": "l711",
                                    "names": "机械设备租赁",
                                    "childrens": [
                                        {
                                            "ids": "l7111",
                                            "names": "汽车租赁"
                                        },
                                        {
                                            "ids": "l7112",
                                            "names": "农业机械租赁"
                                        },
                                        {
                                            "ids": "l7113",
                                            "names": "建筑工程机械与设备租赁"
                                        },
                                        {
                                            "ids": "l7114",
                                            "names": "计算机及通讯设备租赁"
                                        },
                                        {
                                            "ids": "l7119",
                                            "names": "其他机械与设备租赁"
                                        }]
                                },
                                {
                                    "ids": "l712",
                                    "names": "文化及日用品出租",
                                    "childrens": [
                                        {
                                            "ids": "l7121",
                                            "names": "娱乐及体育设备出租"
                                        },
                                        {
                                            "ids": "l7122",
                                            "names": "图书出租"
                                        },
                                        {
                                            "ids": "l7123",
                                            "names": "音像制品出租"
                                        },
                                        {
                                            "ids": "l7129",
                                            "names": "其他文化及日用品出租"
                                        }]
                                }]
                        },
                        {
                            "ids": "l72",
                            "names": "商务服务业",
                            "childrens": [
                                {
                                    "ids": "l721",
                                    "names": "企业管理服务",
                                    "childrens": [
                                        {
                                            "ids": "l7211",
                                            "names": "企业总部管理"
                                        },
                                        {
                                            "ids": "l7212",
                                            "names": "投资与资产管理"
                                        },
                                        {
                                            "ids": "l7213",
                                            "names": "单位后勤管理服务"
                                        },
                                        {
                                            "ids": "l7219",
                                            "names": "其他企业管理服务"
                                        }]
                                },
                                {
                                    "ids": "l722",
                                    "names": "法律服务",
                                    "childrens": [
                                        {
                                            "ids": "l7221",
                                            "names": "律师及相关法律服务"
                                        },
                                        {
                                            "ids": "l7222",
                                            "names": "公证服务"
                                        },
                                        {
                                            "ids": "l7229",
                                            "names": "其他法律服务"
                                        }]
                                },
                                {
                                    "ids": "l723",
                                    "names": "咨询与调查",
                                    "childrens": [
                                        {
                                            "ids": "l7231",
                                            "names": "会计、审计及税务服务"
                                        },
                                        {
                                            "ids": "l7232",
                                            "names": "市场调查"
                                        },
                                        {
                                            "ids": "l7233",
                                            "names": "社会经济咨询"
                                        },
                                        {
                                            "ids": "l7239",
                                            "names": "其他专业咨询"
                                        }]
                                },
                                {
                                    "ids": "l724",
                                    "names": "广告业",
                                    "childrens": [
                                        {
                                            "ids": "l7240",
                                            "names": "广告业"
                                        }]
                                },
                                {
                                    "ids": "l725",
                                    "names": "知识产权服务",
                                    "childrens": [
                                        {
                                            "ids": "l7250",
                                            "names": "知识产权服务"
                                        }]
                                },
                                {
                                    "ids": "l726",
                                    "names": "人力资源服务",
                                    "childrens": [
                                        {
                                            "ids": "l7261",
                                            "names": "公共就业服务"
                                        },
                                        {
                                            "ids": "l7262",
                                            "names": "职业中介服务"
                                        },
                                        {
                                            "ids": "l7263",
                                            "names": "劳务派遣服务"
                                        },
                                        {
                                            "ids": "l7269",
                                            "names": "其他人力资源服务"
                                        }]
                                },
                                {
                                    "ids": "l727",
                                    "names": "旅行社及相关服务",
                                    "childrens": [
                                        {
                                            "ids": "l7271",
                                            "names": "旅行社服务"
                                        },
                                        {
                                            "ids": "l7272",
                                            "names": "旅游管理服务"
                                        },
                                        {
                                            "ids": "l7279",
                                            "names": "其他旅行社相关服务"
                                        }]
                                },
                                {
                                    "ids": "l728",
                                    "names": "安全保护服务",
                                    "childrens": [
                                        {
                                            "ids": "l7281",
                                            "names": "安全服务"
                                        },
                                        {
                                            "ids": "l7282",
                                            "names": "安全系统监控服务"
                                        },
                                        {
                                            "ids": "l7289",
                                            "names": "其他安全保护服务"
                                        }]
                                },
                                {
                                    "ids": "l729",
                                    "names": "其他商务服务业",
                                    "childrens": [
                                        {
                                            "ids": "l7291",
                                            "names": "市场管理"
                                        },
                                        {
                                            "ids": "l7292",
                                            "names": "会议及展览服务"
                                        },
                                        {
                                            "ids": "l7293",
                                            "names": "包装服务"
                                        },
                                        {
                                            "ids": "l7294",
                                            "names": "办公服务"
                                        },
                                        {
                                            "ids": "l7295",
                                            "names": "信用服务"
                                        },
                                        {
                                            "ids": "l7296",
                                            "names": "担保服务"
                                        },
                                        {
                                            "ids": "l7299",
                                            "names": "其他未列明商务服务业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "m",
                    "names": "科学研究和技术服务业",
                    "childrens": [
                        {
                            "ids": "m73",
                            "names": "研究和试验发展",
                            "childrens": [
                                {
                                    "ids": "m731",
                                    "names": "自然科学研究和试验发展",
                                    "childrens": [
                                        {
                                            "ids": "m7310",
                                            "names": "自然科学研究和试验发展"
                                        }]
                                },
                                {
                                    "ids": "m732",
                                    "names": "工程和技术研究和试验发展",
                                    "childrens": [
                                        {
                                            "ids": "m7320",
                                            "names": "工程和技术研究和试验发展"
                                        }]
                                },
                                {
                                    "ids": "m733",
                                    "names": "农业科学研究和试验发展",
                                    "childrens": [
                                        {
                                            "ids": "m7330",
                                            "names": "农业科学研究和试验发展"
                                        }]
                                },
                                {
                                    "ids": "m734",
                                    "names": "医学研究和试验发展",
                                    "childrens": [
                                        {
                                            "ids": "m7340",
                                            "names": "医学研究和试验发展"
                                        }]
                                },
                                {
                                    "ids": "m735",
                                    "names": "社会人文科学研究",
                                    "childrens": [
                                        {
                                            "ids": "m7350",
                                            "names": "社会人文科学研究"
                                        }]
                                }]
                        },
                        {
                            "ids": "m74",
                            "names": "专业技术服务业",
                            "childrens": [
                                {
                                    "ids": "m741",
                                    "names": "气象服务",
                                    "childrens": [
                                        {
                                            "ids": "m7410",
                                            "names": "气象服务"
                                        }]
                                },
                                {
                                    "ids": "m742",
                                    "names": "地震服务",
                                    "childrens": [
                                        {
                                            "ids": "m7420",
                                            "names": "地震服务"
                                        }]
                                },
                                {
                                    "ids": "m743",
                                    "names": "海洋服务",
                                    "childrens": [
                                        {
                                            "ids": "m7430",
                                            "names": "海洋服务"
                                        }]
                                },
                                {
                                    "ids": "m744",
                                    "names": "测绘服务",
                                    "childrens": [
                                        {
                                            "ids": "m7440",
                                            "names": "测绘服务"
                                        }]
                                },
                                {
                                    "ids": "m745",
                                    "names": "质检技术服务",
                                    "childrens": [
                                        {
                                            "ids": "m7450",
                                            "names": "质检技术服务"
                                        }]
                                },
                                {
                                    "ids": "m746",
                                    "names": "环境与生态监测",
                                    "childrens": [
                                        {
                                            "ids": "m7461",
                                            "names": "环境保护监测"
                                        },
                                        {
                                            "ids": "m7462",
                                            "names": "生态监测"
                                        }]
                                },
                                {
                                    "ids": "m747",
                                    "names": "地质勘查",
                                    "childrens": [
                                        {
                                            "ids": "m7471",
                                            "names": "能源矿产地质勘查"
                                        },
                                        {
                                            "ids": "m7472",
                                            "names": "固体矿产地质勘查"
                                        },
                                        {
                                            "ids": "m7473",
                                            "names": "水、二氧化碳等矿产地质勘查"
                                        },
                                        {
                                            "ids": "m7474",
                                            "names": "基础地质勘查"
                                        },
                                        {
                                            "ids": "m7475",
                                            "names": "地质勘查技术服务"
                                        }]
                                },
                                {
                                    "ids": "m748",
                                    "names": "工程技术",
                                    "childrens": [
                                        {
                                            "ids": "m7481",
                                            "names": "工程管理服务"
                                        },
                                        {
                                            "ids": "m7482",
                                            "names": "工程勘察设计"
                                        },
                                        {
                                            "ids": "m7483",
                                            "names": "规划管理"
                                        }]
                                },
                                {
                                    "ids": "m749",
                                    "names": "其他专业技术服务业",
                                    "childrens": [
                                        {
                                            "ids": "m7491",
                                            "names": "专业化设计服务"
                                        },
                                        {
                                            "ids": "m7492",
                                            "names": "摄影扩印服务"
                                        },
                                        {
                                            "ids": "m7493",
                                            "names": "兽医服务"
                                        },
                                        {
                                            "ids": "m7499",
                                            "names": "其他未列明专业技术服务业"
                                        }]
                                }]
                        },
                        {
                            "ids": "m75",
                            "names": "科技推广和应用服务业",
                            "childrens": [
                                {
                                    "ids": "m751",
                                    "names": "技术推广服务",
                                    "childrens": [
                                        {
                                            "ids": "m7511",
                                            "names": "农业技术推广服务"
                                        },
                                        {
                                            "ids": "m7512",
                                            "names": "生物技术推广服务"
                                        },
                                        {
                                            "ids": "m7513",
                                            "names": "新材料技术推广服务"
                                        },
                                        {
                                            "ids": "m7514",
                                            "names": "节能技术推广服务"
                                        },
                                        {
                                            "ids": "m7519",
                                            "names": "其他技术推广服务"
                                        }]
                                },
                                {
                                    "ids": "m752",
                                    "names": "科技中介服务",
                                    "childrens": [
                                        {
                                            "ids": "m7520",
                                            "names": "科技中介服务"
                                        }]
                                },
                                {
                                    "ids": "m759",
                                    "names": "其他科技推广和应用服务业",
                                    "childrens": [
                                        {
                                            "ids": "m7590",
                                            "names": "其他科技推广和应用服务业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "n",
                    "names": "水利、环境和公共设施管理业",
                    "childrens": [
                        {
                            "ids": "n77",
                            "names": "生态保护和环境治理业",
                            "childrens": [
                                {
                                    "ids": "n771",
                                    "names": "生态保护",
                                    "childrens": [
                                        {
                                            "ids": "n7711",
                                            "names": "自然保护区管理"
                                        },
                                        {
                                            "ids": "n7712",
                                            "names": "野生动物保护"
                                        },
                                        {
                                            "ids": "n7713",
                                            "names": "野生植物保护"
                                        },
                                        {
                                            "ids": "n7719",
                                            "names": "其他自然保护"
                                        }]
                                },
                                {
                                    "ids": "n772",
                                    "names": "环境治理业",
                                    "childrens": [
                                        {
                                            "ids": "n7721",
                                            "names": "水污染治理"
                                        },
                                        {
                                            "ids": "n7722",
                                            "names": "大气污染治理"
                                        },
                                        {
                                            "ids": "n7723",
                                            "names": "固体废物治理"
                                        },
                                        {
                                            "ids": "n7724",
                                            "names": "危险废物治理"
                                        },
                                        {
                                            "ids": "n7725",
                                            "names": "放射性废物治理"
                                        },
                                        {
                                            "ids": "n7729",
                                            "names": "其他污染治理"
                                        }]
                                }]
                        },
                        {
                            "ids": "n78",
                            "names": "公共设施管理业",
                            "childrens": [
                                {
                                    "ids": "n781",
                                    "names": "市政设施管理",
                                    "childrens": [
                                        {
                                            "ids": "n7810",
                                            "names": "市政设施管理"
                                        }]
                                },
                                {
                                    "ids": "n782",
                                    "names": "环境卫生管理",
                                    "childrens": [
                                        {
                                            "ids": "n7820",
                                            "names": "环境卫生管理"
                                        }]
                                },
                                {
                                    "ids": "n783",
                                    "names": "城乡市容管理",
                                    "childrens": [
                                        {
                                            "ids": "n7830",
                                            "names": "城乡市容管理"
                                        }]
                                },
                                {
                                    "ids": "n784",
                                    "names": "绿化管理",
                                    "childrens": [
                                        {
                                            "ids": "n7840",
                                            "names": "绿化管理"
                                        }]
                                },
                                {
                                    "ids": "n785",
                                    "names": "公园和游览景区管理",
                                    "childrens": [
                                        {
                                            "ids": "n7851",
                                            "names": "公园管理"
                                        },
                                        {
                                            "ids": "n7852",
                                            "names": "游览景区管理"
                                        }]
                                }]
                        },
                        {
                            "ids": "n76",
                            "names": "水利管理业",
                            "childrens": [
                                {
                                    "ids": "n761",
                                    "names": "防洪除涝设施管理",
                                    "childrens": [
                                        {
                                            "ids": "n7610",
                                            "names": "防洪除涝设施管理"
                                        }]
                                },
                                {
                                    "ids": "n762",
                                    "names": "水资源管理",
                                    "childrens": [
                                        {
                                            "ids": "n7620",
                                            "names": "水资源管理"
                                        }]
                                },
                                {
                                    "ids": "n763",
                                    "names": "天然水收集与分配",
                                    "childrens": [
                                        {
                                            "ids": "n7630",
                                            "names": "天然水收集与分配"
                                        }]
                                },
                                {
                                    "ids": "n764",
                                    "names": "水文服务",
                                    "childrens": [
                                        {
                                            "ids": "n7640",
                                            "names": "水文服务"
                                        }]
                                },
                                {
                                    "ids": "n769",
                                    "names": "其他水利管理业",
                                    "childrens": [
                                        {
                                            "ids": "n7690",
                                            "names": "其他水利管理业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "o",
                    "names": "居民服务、修理和其他服务业",
                    "childrens": [
                        {
                            "ids": "o79",
                            "names": "居民服务业",
                            "childrens": [
                                {
                                    "ids": "o792",
                                    "names": "托儿所服务",
                                    "childrens": [
                                        {
                                            "ids": "o7920",
                                            "names": "托儿所服务"
                                        }]
                                },
                                {
                                    "ids": "o793",
                                    "names": "洗染服务",
                                    "childrens": [
                                        {
                                            "ids": "o7930",
                                            "names": "洗染服务"
                                        }]
                                },
                                {
                                    "ids": "o794",
                                    "names": "理发及美容服务",
                                    "childrens": [
                                        {
                                            "ids": "o7940",
                                            "names": "理发及美容服务"
                                        }]
                                },
                                {
                                    "ids": "o795",
                                    "names": "洗浴服务",
                                    "childrens": [
                                        {
                                            "ids": "o7950",
                                            "names": "洗浴服务"
                                        }]
                                },
                                {
                                    "ids": "o796",
                                    "names": "保健服务",
                                    "childrens": [
                                        {
                                            "ids": "o7960",
                                            "names": "保健服务"
                                        }]
                                },
                                {
                                    "ids": "o797",
                                    "names": "婚姻服务",
                                    "childrens": [
                                        {
                                            "ids": "o7970",
                                            "names": "婚姻服务"
                                        }]
                                },
                                {
                                    "ids": "o798",
                                    "names": "殡葬服务",
                                    "childrens": [
                                        {
                                            "ids": "o7980",
                                            "names": "殡葬服务"
                                        }]
                                },
                                {
                                    "ids": "o799",
                                    "names": "其他居民服务业",
                                    "childrens": [
                                        {
                                            "ids": "o7990",
                                            "names": "其他居民服务业"
                                        }]
                                },
                                {
                                    "ids": "o791",
                                    "names": "家庭服务",
                                    "childrens": [
                                        {
                                            "ids": "o7910",
                                            "names": "家庭服务"
                                        }]
                                }]
                        },
                        {
                            "ids": "o80",
                            "names": "机动车、电子产品和日用产品修理业",
                            "childrens": [
                                {
                                    "ids": "o801",
                                    "names": "汽车、摩托车修理与维护",
                                    "childrens": [
                                        {
                                            "ids": "o8011",
                                            "names": "汽车修理与维护"
                                        },
                                        {
                                            "ids": "o8012",
                                            "names": "摩托车修理与维护"
                                        }]
                                },
                                {
                                    "ids": "o802",
                                    "names": "计算机和办公设备维修",
                                    "childrens": [
                                        {
                                            "ids": "o8021",
                                            "names": "计算机和辅助设备修理"
                                        },
                                        {
                                            "ids": "o8022",
                                            "names": "通讯设备修理"
                                        },
                                        {
                                            "ids": "o8029",
                                            "names": "其他办公设备维修"
                                        }]
                                },
                                {
                                    "ids": "o803",
                                    "names": "家用电器修理",
                                    "childrens": [
                                        {
                                            "ids": "o8031",
                                            "names": "家用电子产品修理"
                                        },
                                        {
                                            "ids": "o8032",
                                            "names": "日用电器修理"
                                        }]
                                },
                                {
                                    "ids": "o809",
                                    "names": "其他日用产品修理业",
                                    "childrens": [
                                        {
                                            "ids": "o8091",
                                            "names": "自行车修理"
                                        },
                                        {
                                            "ids": "o8092",
                                            "names": "鞋和皮革修理"
                                        },
                                        {
                                            "ids": "o8093",
                                            "names": "家具和相关物品修理"
                                        },
                                        {
                                            "ids": "o8099",
                                            "names": "其他未列明日用产品修理业"
                                        }]
                                }]
                        },
                        {
                            "ids": "o81",
                            "names": "其他服务业",
                            "childrens": [
                                {
                                    "ids": "o811",
                                    "names": "清洁服务",
                                    "childrens": [
                                        {
                                            "ids": "o8111",
                                            "names": "建筑物清洁服务"
                                        },
                                        {
                                            "ids": "o8119",
                                            "names": "其他清洁服务"
                                        }]
                                },
                                {
                                    "ids": "o819",
                                    "names": "其他未列明服务业",
                                    "childrens": [
                                        {
                                            "ids": "o8190",
                                            "names": "其他未列明服务业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "p",
                    "names": "教育",
                    "childrens": [
                        {
                            "ids": "p82",
                            "names": "教育",
                            "childrens": [
                                {
                                    "ids": "p821",
                                    "names": "学前教育",
                                    "childrens": [
                                        {
                                            "ids": "p8210",
                                            "names": "学前教育"
                                        }]
                                },
                                {
                                    "ids": "p822",
                                    "names": "初等教育",
                                    "childrens": [
                                        {
                                            "ids": "p8221",
                                            "names": "普通小学教育"
                                        },
                                        {
                                            "ids": "p8222",
                                            "names": "成人小学教育"
                                        }]
                                },
                                {
                                    "ids": "p823",
                                    "names": "中等教育",
                                    "childrens": [
                                        {
                                            "ids": "p8231",
                                            "names": "普通初中教育"
                                        },
                                        {
                                            "ids": "p8232",
                                            "names": "职业初中教育"
                                        },
                                        {
                                            "ids": "p8233",
                                            "names": "成人初中教育"
                                        },
                                        {
                                            "ids": "p8234",
                                            "names": "普通高中教育"
                                        },
                                        {
                                            "ids": "p8235",
                                            "names": "成人高中教育"
                                        },
                                        {
                                            "ids": "p8236",
                                            "names": "中等职业学校教育"
                                        }]
                                },
                                {
                                    "ids": "p824",
                                    "names": "高等教育",
                                    "childrens": [
                                        {
                                            "ids": "p8241",
                                            "names": "普通高等教育"
                                        },
                                        {
                                            "ids": "p8242",
                                            "names": "成人高等教育"
                                        }]
                                },
                                {
                                    "ids": "p825",
                                    "names": "特殊教育",
                                    "childrens": [
                                        {
                                            "ids": "p8250",
                                            "names": "特殊教育"
                                        }]
                                },
                                {
                                    "ids": "p829",
                                    "names": "技能培训、教育辅助及其他教育",
                                    "childrens": [
                                        {
                                            "ids": "p8291",
                                            "names": "职业技能培训"
                                        },
                                        {
                                            "ids": "p8292",
                                            "names": "体校及体育培训"
                                        },
                                        {
                                            "ids": "p8293",
                                            "names": "文化艺术培训"
                                        },
                                        {
                                            "ids": "p8294",
                                            "names": "教育辅助服务"
                                        },
                                        {
                                            "ids": "p8299",
                                            "names": "其他未列明教育"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "q",
                    "names": "卫生和社会工作",
                    "childrens": [
                        {
                            "ids": "q83",
                            "names": "卫生",
                            "childrens": [
                                {
                                    "ids": "q831",
                                    "names": "医院",
                                    "childrens": [
                                        {
                                            "ids": "q8311",
                                            "names": "综合医院"
                                        },
                                        {
                                            "ids": "q8312",
                                            "names": "中医医院"
                                        },
                                        {
                                            "ids": "q8313",
                                            "names": "中西医结合医院"
                                        },
                                        {
                                            "ids": "q8314",
                                            "names": "民族医院"
                                        },
                                        {
                                            "ids": "q8315",
                                            "names": "专科医院"
                                        },
                                        {
                                            "ids": "q8316",
                                            "names": "疗养院"
                                        }]
                                },
                                {
                                    "ids": "q832",
                                    "names": "社区医疗与卫生院",
                                    "childrens": [
                                        {
                                            "ids": "q8321",
                                            "names": "社区卫生服务中心（站）"
                                        },
                                        {
                                            "ids": "q8322",
                                            "names": "街道卫生院"
                                        },
                                        {
                                            "ids": "q8323",
                                            "names": "乡镇卫生院"
                                        }]
                                },
                                {
                                    "ids": "q833",
                                    "names": "门诊部（所）",
                                    "childrens": [
                                        {
                                            "ids": "q8330",
                                            "names": "门诊部（所）"
                                        }]
                                },
                                {
                                    "ids": "q834",
                                    "names": "计划生育技术服务活动",
                                    "childrens": [
                                        {
                                            "ids": "q8340",
                                            "names": "计划生育技术服务活动"
                                        }]
                                },
                                {
                                    "ids": "q835",
                                    "names": "妇幼保健院（所、站）",
                                    "childrens": [
                                        {
                                            "ids": "q8350",
                                            "names": "妇幼保健院（所、站）"
                                        }]
                                },
                                {
                                    "ids": "q836",
                                    "names": "专科疾病防治院（所、站）",
                                    "childrens": [
                                        {
                                            "ids": "q8360",
                                            "names": "专科疾病防治院（所、站）"
                                        }]
                                },
                                {
                                    "ids": "q837",
                                    "names": "疾病预防控制中心",
                                    "childrens": [
                                        {
                                            "ids": "q8370",
                                            "names": "疾病预防控制中心"
                                        }]
                                },
                                {
                                    "ids": "q839",
                                    "names": "其他卫生活动",
                                    "childrens": [
                                        {
                                            "ids": "q8390",
                                            "names": "其他卫生活动"
                                        }]
                                }]
                        },
                        {
                            "ids": "q84",
                            "names": "社会工作",
                            "childrens": [
                                {
                                    "ids": "q841",
                                    "names": "提供住宿社会工作",
                                    "childrens": [
                                        {
                                            "ids": "q8411",
                                            "names": "干部休养所"
                                        },
                                        {
                                            "ids": "q8412",
                                            "names": "护理机构服务"
                                        },
                                        {
                                            "ids": "q8413",
                                            "names": "精神康复服务"
                                        },
                                        {
                                            "ids": "q8414",
                                            "names": "老年人、残疾人养护服务"
                                        },
                                        {
                                            "ids": "q8415",
                                            "names": "孤残儿童收养和庇护服务"
                                        },
                                        {
                                            "ids": "q8419",
                                            "names": "其他提供住宿社会救助"
                                        }]
                                },
                                {
                                    "ids": "q842",
                                    "names": "不提供住宿社会工作",
                                    "childrens": [
                                        {
                                            "ids": "q8421",
                                            "names": "社会看护与帮助服务"
                                        },
                                        {
                                            "ids": "q8429",
                                            "names": "其他不提供住宿社会工作"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "r",
                    "names": "文化、体育和娱乐业",
                    "childrens": [
                        {
                            "ids": "r85",
                            "names": "新闻和出版业",
                            "childrens": [
                                {
                                    "ids": "r851",
                                    "names": "新闻业",
                                    "childrens": [
                                        {
                                            "ids": "r8510",
                                            "names": "新闻业"
                                        }]
                                },
                                {
                                    "ids": "r852",
                                    "names": "出版业",
                                    "childrens": [
                                        {
                                            "ids": "r8524",
                                            "names": "音像制品出版"
                                        },
                                        {
                                            "ids": "r8525",
                                            "names": "电子出版物出版"
                                        },
                                        {
                                            "ids": "r8529",
                                            "names": "其他出版业"
                                        },
                                        {
                                            "ids": "r8521",
                                            "names": "图书出版"
                                        },
                                        {
                                            "ids": "r8522",
                                            "names": "报纸出版"
                                        },
                                        {
                                            "ids": "r8523",
                                            "names": "期刊出版"
                                        }]
                                }]
                        },
                        {
                            "ids": "r86",
                            "names": "广播、电视、电影和影视录音制作业",
                            "childrens": [
                                {
                                    "ids": "r861",
                                    "names": "广播",
                                    "childrens": [
                                        {
                                            "ids": "r8610",
                                            "names": "广播"
                                        }]
                                },
                                {
                                    "ids": "r862",
                                    "names": "电视",
                                    "childrens": [
                                        {
                                            "ids": "r8620",
                                            "names": "电视"
                                        }]
                                },
                                {
                                    "ids": "r863",
                                    "names": "电影和影视节目制作",
                                    "childrens": [
                                        {
                                            "ids": "r8630",
                                            "names": "电影和影视节目制作"
                                        }]
                                },
                                {
                                    "ids": "r864",
                                    "names": "电影和影视节目发行",
                                    "childrens": [
                                        {
                                            "ids": "r8640",
                                            "names": "电影和影视节目发行"
                                        }]
                                },
                                {
                                    "ids": "r865",
                                    "names": "电影放映",
                                    "childrens": [
                                        {
                                            "ids": "r8650",
                                            "names": "电影放映"
                                        }]
                                },
                                {
                                    "ids": "r866",
                                    "names": "录音制作",
                                    "childrens": [
                                        {
                                            "ids": "r8660",
                                            "names": "录音制作"
                                        }]
                                }]
                        },
                        {
                            "ids": "r87",
                            "names": "文化艺术业",
                            "childrens": [
                                {
                                    "ids": "r871",
                                    "names": "文艺创作与表演",
                                    "childrens": [
                                        {
                                            "ids": "r8710",
                                            "names": "文艺创作与表演"
                                        }]
                                },
                                {
                                    "ids": "r872",
                                    "names": "艺术表演场馆",
                                    "childrens": [
                                        {
                                            "ids": "r8720",
                                            "names": "艺术表演场馆"
                                        }]
                                },
                                {
                                    "ids": "r873",
                                    "names": "图书馆与档案馆",
                                    "childrens": [
                                        {
                                            "ids": "r8731",
                                            "names": "图书馆"
                                        },
                                        {
                                            "ids": "r8732",
                                            "names": "档案馆"
                                        }]
                                },
                                {
                                    "ids": "r874",
                                    "names": "文物及非物质文化遗产保护",
                                    "childrens": [
                                        {
                                            "ids": "r8740",
                                            "names": "文物及非物质文化遗产保护"
                                        }]
                                },
                                {
                                    "ids": "r875",
                                    "names": "博物馆",
                                    "childrens": [
                                        {
                                            "ids": "r8750",
                                            "names": "博物馆"
                                        }]
                                },
                                {
                                    "ids": "r876",
                                    "names": "烈士陵园、纪念馆",
                                    "childrens": [
                                        {
                                            "ids": "r8760",
                                            "names": "烈士陵园、纪念馆"
                                        }]
                                },
                                {
                                    "ids": "r877",
                                    "names": "群众文化活动",
                                    "childrens": [
                                        {
                                            "ids": "r8770",
                                            "names": "群众文化活动"
                                        }]
                                },
                                {
                                    "ids": "r879",
                                    "names": "其他文化艺术业",
                                    "childrens": [
                                        {
                                            "ids": "r8790",
                                            "names": "其他文化艺术业"
                                        }]
                                }]
                        },
                        {
                            "ids": "r88",
                            "names": "体育",
                            "childrens": [
                                {
                                    "ids": "r881",
                                    "names": "体育组织",
                                    "childrens": [
                                        {
                                            "ids": "r8810",
                                            "names": "体育组织"
                                        }]
                                },
                                {
                                    "ids": "r882",
                                    "names": "体育场馆",
                                    "childrens": [
                                        {
                                            "ids": "r8820",
                                            "names": "体育场馆"
                                        }]
                                },
                                {
                                    "ids": "r883",
                                    "names": "休闲健身活动",
                                    "childrens": [
                                        {
                                            "ids": "r8830",
                                            "names": "休闲健身活动"
                                        }]
                                },
                                {
                                    "ids": "r889",
                                    "names": "其他体育",
                                    "childrens": [
                                        {
                                            "ids": "r8890",
                                            "names": "其他体育"
                                        }]
                                }]
                        },
                        {
                            "ids": "r89",
                            "names": "娱乐业",
                            "childrens": [
                                {
                                    "ids": "r891",
                                    "names": "室内娱乐活动",
                                    "childrens": [
                                        {
                                            "ids": "r8911",
                                            "names": "歌舞厅娱乐活动"
                                        },
                                        {
                                            "ids": "r8912",
                                            "names": "电子游艺厅娱乐活动"
                                        },
                                        {
                                            "ids": "r8913",
                                            "names": "网吧活动"
                                        },
                                        {
                                            "ids": "r8919",
                                            "names": "其他室内娱乐活动"
                                        }]
                                },
                                {
                                    "ids": "r892",
                                    "names": "游乐园",
                                    "childrens": [
                                        {
                                            "ids": "r8920",
                                            "names": "游乐园"
                                        }]
                                },
                                {
                                    "ids": "r893",
                                    "names": "彩票活动",
                                    "childrens": [
                                        {
                                            "ids": "r8930",
                                            "names": "彩票活动"
                                        }]
                                },
                                {
                                    "ids": "r894",
                                    "names": "文化、娱乐、体育经纪代理",
                                    "childrens": [
                                        {
                                            "ids": "r8941",
                                            "names": "文化娱乐经纪人"
                                        },
                                        {
                                            "ids": "r8942",
                                            "names": "体育经纪人"
                                        },
                                        {
                                            "ids": "r8949",
                                            "names": "其他文化艺术经纪代理"
                                        }]
                                },
                                {
                                    "ids": "r899",
                                    "names": "其他娱乐业",
                                    "childrens": [
                                        {
                                            "ids": "r8990",
                                            "names": "其他娱乐业"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "s",
                    "names": "公共管理、社会保障和社会组织",
                    "childrens": [
                        {
                            "ids": "s90",
                            "names": "中国共产党机关",
                            "childrens": [
                                {
                                    "ids": "s900",
                                    "names": "中国共产党机关",
                                    "childrens": [
                                        {
                                            "ids": "s9000",
                                            "names": "中国共产党机关"
                                        }]
                                }]
                        },
                        {
                            "ids": "s91",
                            "names": "国家机构",
                            "childrens": [
                                {
                                    "ids": "s911",
                                    "names": "国家权力机构",
                                    "childrens": [
                                        {
                                            "ids": "s9110",
                                            "names": "国家权力机构"
                                        }]
                                },
                                {
                                    "ids": "s912",
                                    "names": "国家行政机构",
                                    "childrens": [
                                        {
                                            "ids": "s9121",
                                            "names": "综合事务管理机构"
                                        },
                                        {
                                            "ids": "s9122",
                                            "names": "对外事务管理机构"
                                        },
                                        {
                                            "ids": "s9123",
                                            "names": "公共安全管理机构"
                                        },
                                        {
                                            "ids": "s9124",
                                            "names": "社会事务管理机构"
                                        },
                                        {
                                            "ids": "s9125",
                                            "names": "经济事务管理机构"
                                        },
                                        {
                                            "ids": "s9126",
                                            "names": "行政监督检查机构"
                                        }]
                                },
                                {
                                    "ids": "s913",
                                    "names": "人民法院和人民检察院",
                                    "childrens": [
                                        {
                                            "ids": "s9131",
                                            "names": "人民法院"
                                        },
                                        {
                                            "ids": "s9132",
                                            "names": "人民检察院"
                                        }]
                                },
                                {
                                    "ids": "s919",
                                    "names": "其他国家机构",
                                    "childrens": [
                                        {
                                            "ids": "s9190",
                                            "names": "其他国家机构"
                                        }]
                                }]
                        },
                        {
                            "ids": "s92",
                            "names": "人民政协、民主党派",
                            "childrens": [
                                {
                                    "ids": "s921",
                                    "names": "人民政协",
                                    "childrens": [
                                        {
                                            "ids": "s9210",
                                            "names": "人民政协"
                                        }]
                                },
                                {
                                    "ids": "s922",
                                    "names": "民主党派",
                                    "childrens": [
                                        {
                                            "ids": "s9220",
                                            "names": "民主党派"
                                        }]
                                }]
                        },
                        {
                            "ids": "s93",
                            "names": "社会保障",
                            "childrens": [
                                {
                                    "ids": "s930",
                                    "names": "社会保障",
                                    "childrens": [
                                        {
                                            "ids": "s9300",
                                            "names": "社会保障"
                                        }]
                                }]
                        },
                        {
                            "ids": "s94",
                            "names": "群众团体、社会团体和其他成员组织",
                            "childrens": [
                                {
                                    "ids": "s941",
                                    "names": "群众团体",
                                    "childrens": [
                                        {
                                            "ids": "s9411",
                                            "names": "工会"
                                        },
                                        {
                                            "ids": "s9412",
                                            "names": "妇联"
                                        },
                                        {
                                            "ids": "s9413",
                                            "names": "共青团"
                                        },
                                        {
                                            "ids": "s9419",
                                            "names": "其他群众团体"
                                        }]
                                },
                                {
                                    "ids": "s942",
                                    "names": "社会团体",
                                    "childrens": [
                                        {
                                            "ids": "s9421",
                                            "names": "专业性团体"
                                        },
                                        {
                                            "ids": "s9422",
                                            "names": "行业性团体"
                                        },
                                        {
                                            "ids": "s9429",
                                            "names": "其他社会团体"
                                        }]
                                },
                                {
                                    "ids": "s943",
                                    "names": "基金会",
                                    "childrens": [
                                        {
                                            "ids": "s9430",
                                            "names": "基金会"
                                        }]
                                },
                                {
                                    "ids": "s944",
                                    "names": "宗教组织",
                                    "childrens": [
                                        {
                                            "ids": "s9440",
                                            "names": "宗教组织"
                                        }]
                                }]
                        },
                        {
                            "ids": "s95",
                            "names": "基层群众自治组织",
                            "childrens": [
                                {
                                    "ids": "s951",
                                    "names": "社区自治组织",
                                    "childrens": [
                                        {
                                            "ids": "s9510",
                                            "names": "社区自治组织"
                                        }]
                                },
                                {
                                    "ids": "s952",
                                    "names": "村民自治组织",
                                    "childrens": [
                                        {
                                            "ids": "s9520",
                                            "names": "村民自治组织"
                                        }]
                                }]
                        }]
                },
                {
                    "ids": "t",
                    "names": "国际组织",
                    "childrens": [
                        {
                            "ids": "t96",
                            "names": "国际组织",
                            "childrens": [
                                {
                                    "ids": "t960",
                                    "names": "国际组织",
                                    "childrens": [
                                        {
                                            "ids": "t9600",
                                            "names": "国际组织"
                                        }]
                                }]
                        }]
                }];
        }
	};

	exports('industry', industry);
});

//生成的code
// var code1 = new Array(
//     new Array("a","农、林、牧、渔业"),
//     new Array("b","采矿业"),
//     new Array("c","制造业"),
//     new Array("d","电力、热力、燃气及水生产和供应业"),
//     new Array("e","建筑业"),
//     new Array("f","批发和零售业"),
//     new Array("g","交通运输、仓储和邮政业"),
//     new Array("h","住宿和餐饮业"),
//     new Array("i","信息传输、软件和信息技术服务业"),
//     new Array("j","金融业"),
//     new Array("k","房地产业"),
//     new Array("l","租赁和商务服务业"),
//     new Array("m","科学研究和技术服务业"),
//     new Array("n","水利、环境和公共设施管理业"),
//     new Array("o","居民服务、修理和其他服务业"),
//     new Array("p","教育"),
//     new Array("q","卫生和社会工作"),
//     new Array("r","文化、体育和娱乐业"),
//     new Array("s","公共管理、社会保障和社会组织"),
//     new Array("t","国际组织"));
//
// var industry = {};
//
// for(var i = 0; i<code1.length;i++){industry[code1[i][0]]={ids:code1[i][0],name:code1[i][1]};};
//
// var code2 = new Array(
//     new Array("q83","卫生"),
//     new Array("q84","社会工作"),
//     new Array("r85","新闻和出版业"),
//     new Array("r86","广播、电视、电影和影视录音制作业"),
//     new Array("r87","文化艺术业"),
//     new Array("r88","体育"),
//     new Array("r89","娱乐业"),
//     new Array("k70","房地产业"),
//     new Array("j68","保险业"),
//     new Array("j66","货币金融服务"),
//     new Array("j67","资本市场服务"),
//     new Array("j69","其他金融业"),
//     new Array("l71","租赁业"),
//     new Array("l72","商务服务业"),
//     new Array("e50","建筑装饰和其他建筑业"),
//     new Array("e47","房屋建筑业"),
//     new Array("e48","土木工程建筑业"),
//     new Array("e49","建筑安装业"),
//     new Array("s90","中国共产党机关"),
//     new Array("s91","国家机构"),
//     new Array("s92","人民政协、民主党派"),
//     new Array("s93","社会保障"),
//     new Array("s94","群众团体、社会团体和其他成员组织"),
//     new Array("s95","基层群众自治组织"),
//     new Array("p82","教育"),
//     new Array("o79","居民服务业"),
//     new Array("o80","机动车、电子产品和日用产品修理业"),
//     new Array("o81","其他服务业"),
//     new Array("d44","电力、热力生产和供应业"),
//     new Array("d45","燃气生产和供应业"),
//     new Array("d46","水的生产和供应业"),
//     new Array("i63","电信、广播电视和卫星传输服务"),
//     new Array("i64","互联网和相关服务"),
//     new Array("i65","软件和信息技术服务业"),
//     new Array("n77","生态保护和环境治理业"),
//     new Array("n78","公共设施管理业"),
//     new Array("n76","水利管理业"),
//     new Array("g55","水上运输业"),
//     new Array("g53","铁路运输业"),
//     new Array("g54","道路运输业"),
//     new Array("g56","航空运输业"),
//     new Array("g57","管道运输业"),
//     new Array("g58","装卸搬运和运输代理业"),
//     new Array("g59","仓储业"),
//     new Array("g60","邮政业"),
//     new Array("m73","研究和试验发展"),
//     new Array("m74","专业技术服务业"),
//     new Array("m75","科技推广和应用服务业"),
//     new Array("c13","农副食品加工业"),
//     new Array("c14","食品制造业"),
//     new Array("c15","酒、饮料和精制茶制造业"),
//     new Array("c16","烟草制品业"),
//     new Array("c17","纺织业"),
//     new Array("c18","纺织服装、服饰业"),
//     new Array("c19","皮革、毛皮、羽毛及其制品和制鞋业"),
//     new Array("c20","木材加工和木、竹、藤、棕、草制品业"),
//     new Array("c21","家具制造业"),
//     new Array("c22","造纸和纸制品业"),
//     new Array("c23","印刷和记录媒介复制业"),
//     new Array("c24","文教、工美、体育和娱乐用品制造业"),
//     new Array("c25","石油加工、炼焦和核燃料加工业"),
//     new Array("c26","化学原料和化学制品制造业"),
//     new Array("c27","医药制造业"),
//     new Array("c28","化学纤维制造业"),
//     new Array("c29","橡胶和塑料制品业"),
//     new Array("c30","非金属矿物制品业"),
//     new Array("c31","黑色金属冶炼和压延加工业"),
//     new Array("c32","有色金属冶炼和压延加工业"),
//     new Array("c33","金属制品业"),
//     new Array("c35","专用设备制造业"),
//     new Array("c34","通用设备制造业"),
//     new Array("c36","汽车制造业"),
//     new Array("c37","铁路、船舶、航空航天和其他运输设备制造业"),
//     new Array("c38","电气机械和器材制造业"),
//     new Array("c39","计算机、通信和其他电子设备制造业"),
//     new Array("c40","仪器仪表制造业"),
//     new Array("c41","其他制造业"),
//     new Array("c42","废弃资源综合利用业"),
//     new Array("c43","金属制品、机械和设备修理业"),
//     new Array("f52","零售业"),
//     new Array("f51","批发业"),
//     new Array("h61","住宿业"),
//     new Array("h62","餐饮业"),
//     new Array("a01","农业"),
//     new Array("a02","林业"),
//     new Array("a03","畜牧业"),
//     new Array("a04","渔业"),
//     new Array("a05","农、林、牧、渔服务业"),
//     new Array("t96","国际组织"),
//     new Array("b06","煤炭开采和洗选业"),
//     new Array("b07","石油和天然气开采业"),
//     new Array("b08","黑色金属矿采选业"),
//     new Array("b09","有色金属矿采选业"),
//     new Array("b10","非金属矿采选业"),
//     new Array("b11","开采辅助活动"),
//     new Array("b12","其他采矿业"));
//
// var industryCode2 = {};
//
// for(var i = 0; i<code2.length;i++){industryCode2[code2[i][0]]={ids:code2[i][0],name:code2[i][1]};};
//
// var code3 = new Array(
//     new Array("q831","医院"),
//     new Array("q832","社区医疗与卫生院"),
//     new Array("q833","门诊部（所）"),
//     new Array("q834","计划生育技术服务活动"),
//     new Array("q835","妇幼保健院（所、站）"),
//     new Array("q836","专科疾病防治院（所、站）"),
//     new Array("q837","疾病预防控制中心"),
//     new Array("q839","其他卫生活动"),
//     new Array("q841","提供住宿社会工作"),
//     new Array("q842","不提供住宿社会工作"),
//     new Array("r851","新闻业"),
//     new Array("r852","出版业"),
//     new Array("r861","广播"),
//     new Array("r862","电视"),
//     new Array("r863","电影和影视节目制作"),
//     new Array("r864","电影和影视节目发行"),
//     new Array("r865","电影放映"),
//     new Array("r866","录音制作"),
//     new Array("r871","文艺创作与表演"),
//     new Array("r872","艺术表演场馆"),
//     new Array("r873","图书馆与档案馆"),
//     new Array("r874","文物及非物质文化遗产保护"),
//     new Array("r875","博物馆"),
//     new Array("r876","烈士陵园、纪念馆"),
//     new Array("r877","群众文化活动"),
//     new Array("r879","其他文化艺术业"),
//     new Array("r881","体育组织"),
//     new Array("r882","体育场馆"),
//     new Array("r883","休闲健身活动"),
//     new Array("r889","其他体育"),
//     new Array("r891","室内娱乐活动"),
//     new Array("r892","游乐园"),
//     new Array("r893","彩票活动"),
//     new Array("r894","文化、娱乐、体育经纪代理"),
//     new Array("r899","其他娱乐业"),
//     new Array("k701","房地产开发经营"),
//     new Array("k702","物业管理"),
//     new Array("k703","房地产中介服务"),
//     new Array("k704","自有房地产经营活动"),
//     new Array("k709","其他房地产业"),
//     new Array("j681","人身保险"),
//     new Array("j682","财产保险"),
//     new Array("j683","再保险"),
//     new Array("j684","养老金"),
//     new Array("j685","保险经纪与代理服务"),
//     new Array("j686","保险监管服务"),
//     new Array("j689","其他保险活动"),
//     new Array("j661","中央银行服务"),
//     new Array("j662","货币银行服务"),
//     new Array("j663","非货币银行服务"),
//     new Array("j664","银行监管服务"),
//     new Array("j672","期货市场服务"),
//     new Array("j673","证券期货监管服务"),
//     new Array("j674","资本投资服务"),
//     new Array("j679","其他资本市场服务"),
//     new Array("j671","证券市场服务"),
//     new Array("j691","金融信托与管理服务"),
//     new Array("j692","控股公司服务"),
//     new Array("j693","非金融机构支付服务"),
//     new Array("j694","金融信息服务"),
//     new Array("j699","其他未列明金融业"),
//     new Array("l711","机械设备租赁"),
//     new Array("l712","文化及日用品出租"),
//     new Array("l721","企业管理服务"),
//     new Array("l722","法律服务"),
//     new Array("l723","咨询与调查"),
//     new Array("l724","广告业"),
//     new Array("l725","知识产权服务"),
//     new Array("l726","人力资源服务"),
//     new Array("l727","旅行社及相关服务"),
//     new Array("l728","安全保护服务"),
//     new Array("l729","其他商务服务业"),
//     new Array("e501","建筑装饰业"),
//     new Array("e502","工程准备活动"),
//     new Array("e503","提供施工设备服务"),
//     new Array("e509","其他未列明建筑业"),
//     new Array("e470","房屋建筑业"),
//     new Array("e481","铁路、道路、隧道和桥梁工程建筑"),
//     new Array("e482","水利和内河港口工程建筑"),
//     new Array("e483","海洋工程建筑"),
//     new Array("e484","工矿工程建筑"),
//     new Array("e485","架线和管道工程建筑"),
//     new Array("e489","其他土木工程建筑"),
//     new Array("e491","电气安装"),
//     new Array("e492","管道和设备安装"),
//     new Array("e499","其他建筑安装业"),
//     new Array("s900","中国共产党机关"),
//     new Array("s911","国家权力机构"),
//     new Array("s912","国家行政机构"),
//     new Array("s913","人民法院和人民检察院"),
//     new Array("s919","其他国家机构"),
//     new Array("s921","人民政协"),
//     new Array("s922","民主党派"),
//     new Array("s930","社会保障"),
//     new Array("s941","群众团体"),
//     new Array("s942","社会团体"),
//     new Array("s943","基金会"),
//     new Array("s944","宗教组织"),
//     new Array("s951","社区自治组织"),
//     new Array("s952","村民自治组织"),
//     new Array("p821","学前教育"),
//     new Array("p822","初等教育"),
//     new Array("p823","中等教育"),
//     new Array("p824","高等教育"),
//     new Array("p825","特殊教育"),
//     new Array("p829","技能培训、教育辅助及其他教育"),
//     new Array("o792","托儿所服务"),
//     new Array("o793","洗染服务"),
//     new Array("o794","理发及美容服务"),
//     new Array("o795","洗浴服务"),
//     new Array("o796","保健服务"),
//     new Array("o797","婚姻服务"),
//     new Array("o798","殡葬服务"),
//     new Array("o799","其他居民服务业"),
//     new Array("o791","家庭服务"),
//     new Array("o801","汽车、摩托车修理与维护"),
//     new Array("o802","计算机和办公设备维修"),
//     new Array("o803","家用电器修理"),
//     new Array("o809","其他日用产品修理业"),
//     new Array("o811","清洁服务"),
//     new Array("o819","其他未列明服务业"),
//     new Array("d441","电力生产"),
//     new Array("d442","电力供应"),
//     new Array("d443","热力生产和供应"),
//     new Array("d450","燃气生产和供应业"),
//     new Array("d461","自来水生产和供应"),
//     new Array("d462","污水处理及其再生利用"),
//     new Array("d469","其他水的处理、利用与分配"),
//     new Array("i631","电信"),
//     new Array("i632","广播电视传输服务"),
//     new Array("i633","卫星传输服务"),
//     new Array("i641","互联网接入及相关服务"),
//     new Array("i642","互联网信息服务"),
//     new Array("i649","其他互联网服务"),
//     new Array("i654","数据处理和存储服务"),
//     new Array("i655","集成电路设计"),
//     new Array("i659","其他信息技术服务业"),
//     new Array("i651","软件开发"),
//     new Array("i652","信息系统集成服务"),
//     new Array("i653","信息技术咨询服务"),
//     new Array("n771","生态保护"),
//     new Array("n772","环境治理业"),
//     new Array("n781","市政设施管理"),
//     new Array("n782","环境卫生管理"),
//     new Array("n783","城乡市容管理"),
//     new Array("n784","绿化管理"),
//     new Array("n785","公园和游览景区管理"),
//     new Array("n761","防洪除涝设施管理"),
//     new Array("n762","水资源管理"),
//     new Array("n763","天然水收集与分配"),
//     new Array("n764","水文服务"),
//     new Array("n769","其他水利管理业"),
//     new Array("g551","水上旅客运输"),
//     new Array("g552","水上货物运输"),
//     new Array("g553","水上运输辅助活动"),
//     new Array("g531","铁路旅客运输"),
//     new Array("g532","铁路货物运输"),
//     new Array("g533","铁路运输辅助活动"),
//     new Array("g542","公路旅客运输"),
//     new Array("g543","道路货物运输"),
//     new Array("g544","道路运输辅助活动"),
//     new Array("g541","城市公共交通运输"),
//     new Array("g561","航空客货运输"),
//     new Array("g562","通用航空服务"),
//     new Array("g563","航空运输辅助活动"),
//     new Array("g570","管道运输业"),
//     new Array("g581","装卸搬运"),
//     new Array("g582","运输代理业"),
//     new Array("g591","谷物、棉花等农产品仓储"),
//     new Array("g599","其他仓储业"),
//     new Array("g601","邮政基本服务"),
//     new Array("g602","快递服务"),
//     new Array("m731","自然科学研究和试验发展"),
//     new Array("m732","工程和技术研究和试验发展"),
//     new Array("m733","农业科学研究和试验发展"),
//     new Array("m734","医学研究和试验发展"),
//     new Array("m735","社会人文科学研究"),
//     new Array("m741","气象服务"),
//     new Array("m742","地震服务"),
//     new Array("m743","海洋服务"),
//     new Array("m744","测绘服务"),
//     new Array("m745","质检技术服务"),
//     new Array("m746","环境与生态监测"),
//     new Array("m747","地质勘查"),
//     new Array("m748","工程技术"),
//     new Array("m749","其他专业技术服务业"),
//     new Array("m751","技术推广服务"),
//     new Array("m752","科技中介服务"),
//     new Array("m759","其他科技推广和应用服务业"),
//     new Array("c131","谷物磨制"),
//     new Array("c132","饲料加工"),
//     new Array("c133","植物油加工"),
//     new Array("c134","制糖业"),
//     new Array("c135","屠宰及肉类加工"),
//     new Array("c136","水产品加工"),
//     new Array("c137","蔬菜、水果和坚果加工"),
//     new Array("c139","其他农副食品加工"),
//     new Array("c141","焙烤食品制造"),
//     new Array("c142","糖果、巧克力及蜜饯制造"),
//     new Array("c143","方便食品制造"),
//     new Array("c144","乳制品制造"),
//     new Array("c145","罐头食品制造"),
//     new Array("c146","调味品、发酵制品制造"),
//     new Array("c149","其他食品制造"),
//     new Array("c152","饮料制造"),
//     new Array("c151","酒的制造"),
//     new Array("c153","精制茶加工"),
//     new Array("c161","烟叶复烤"),
//     new Array("c162","卷烟制造"),
//     new Array("c169","其他烟草制品制造"),
//     new Array("c171","棉纺织及印染精加工"),
//     new Array("c172","毛纺织及染整精加工"),
//     new Array("c173","麻纺织及染整精加工"),
//     new Array("c174","丝绢纺织及印染精加工"),
//     new Array("c175","化纤织造及印染精加工"),
//     new Array("c176","针织或钩针编织物及其制品制造"),
//     new Array("c177","家用纺织制成品制造"),
//     new Array("c178","非家用纺织制成品制造"),
//     new Array("c181","机织服装制造"),
//     new Array("c182","针织或钩针编织服装制造"),
//     new Array("c183","服饰制造"),
//     new Array("c191","皮革鞣制加工"),
//     new Array("c192","皮革制品制造"),
//     new Array("c193","毛皮鞣制及制品加工"),
//     new Array("c194","羽毛(绒)加工及制品制造"),
//     new Array("c195","制鞋业"),
//     new Array("c201","木材加工"),
//     new Array("c202","人造板制造"),
//     new Array("c204","竹、藤、棕、草等制品制造"),
//     new Array("c203","木制品制造"),
//     new Array("c211","木质家具制造"),
//     new Array("c212","竹、藤家具制造"),
//     new Array("c213","金属家具制造"),
//     new Array("c214","塑料家具制造"),
//     new Array("c219","其他家具制造"),
//     new Array("c221","纸浆制造"),
//     new Array("c222","造纸"),
//     new Array("c223","纸制品制造"),
//     new Array("c231","印刷"),
//     new Array("c232","装订及印刷相关服务"),
//     new Array("c233","记录媒介复制"),
//     new Array("c241","文教办公用品制造"),
//     new Array("c242","乐器制造"),
//     new Array("c243","工艺美术品制造"),
//     new Array("c244","体育用品制造"),
//     new Array("c245","玩具制造"),
//     new Array("c246","游艺器材及娱乐用品制造"),
//     new Array("c251","精炼石油产品制造"),
//     new Array("c252","炼焦"),
//     new Array("c253","核燃料加工"),
//     new Array("c261","基础化学原料制造"),
//     new Array("c262","肥料制造"),
//     new Array("c263","农药制造"),
//     new Array("c264","涂料、油墨、颜料及类似产品制造"),
//     new Array("c265","合成材料制造"),
//     new Array("c266","专用化学产品制造"),
//     new Array("c267","炸药、火工及焰火产品制造"),
//     new Array("c268","日用化学产品制造"),
//     new Array("c271","化学药品原料药制造"),
//     new Array("c272","化学药品制剂制造"),
//     new Array("c273","中药饮片加工"),
//     new Array("c274","中成药生产"),
//     new Array("c275","兽用药品制造"),
//     new Array("c276","生物药品制造"),
//     new Array("c277","卫生材料及医药用品制造"),
//     new Array("c281","纤维素纤维原料及纤维制造"),
//     new Array("c282","合成纤维制造"),
//     new Array("c291","橡胶制品业"),
//     new Array("c292","塑料制品业"),
//     new Array("c301","水泥、石灰和石膏制造"),
//     new Array("c302","石膏、水泥制品及类似制品制造"),
//     new Array("c303","砖瓦、石材等建筑材料制造"),
//     new Array("c304","玻璃制造"),
//     new Array("c305","玻璃制品制造"),
//     new Array("c306","玻璃纤维和玻璃纤维增强塑料制品制造"),
//     new Array("c307","陶瓷制品制造"),
//     new Array("c308","耐火材料制品制造"),
//     new Array("c309","石墨及其他非金属矿物制品制造"),
//     new Array("c314","钢压延加工"),
//     new Array("c315","铁合金冶炼"),
//     new Array("c311","炼铁"),
//     new Array("c312","炼钢"),
//     new Array("c313","黑色金属铸造"),
//     new Array("c321","常用有色金属冶炼"),
//     new Array("c322","贵金属冶炼"),
//     new Array("c323","稀有稀土金属冶炼"),
//     new Array("c324","有色金属合金制造"),
//     new Array("c325","有色金属铸造"),
//     new Array("c326","有色金属压延加工"),
//     new Array("c331","结构性金属制品制造"),
//     new Array("c332","金属工具制造"),
//     new Array("c333","集装箱及金属包装容器制造"),
//     new Array("c334","金属丝绳及其制品制造"),
//     new Array("c335","建筑、安全用金属制品制造"),
//     new Array("c336","金属表面处理及热处理加工"),
//     new Array("c337","搪瓷制品制造"),
//     new Array("c338","金属制日用品制造"),
//     new Array("c339","其他金属制品制造"),
//     new Array("c351","采矿、冶金、建筑专用设备制造"),
//     new Array("c352","化工、木材、非金属加工专用设备制造"),
//     new Array("c353","食品、饮料、烟草及饲料生产专用设备制造"),
//     new Array("c354","印刷、制药、日化及日用品生产专用设备制造"),
//     new Array("c355","纺织、服装和皮革加工专用设备制造"),
//     new Array("c356","电子和电工机械专用设备制造"),
//     new Array("c357","农、林、牧、渔专用机械制造"),
//     new Array("c358","医疗仪器设备及器械制造"),
//     new Array("c359","环保、社会公共服务及其他专用设备制造"),
//     new Array("c345","轴承、齿轮和传动部件制造"),
//     new Array("c346","烘炉、风机、衡器、包装等设备制造"),
//     new Array("c347","文化、办公用机械制造"),
//     new Array("c348","通用零部件制造"),
//     new Array("c349","其他通用设备制造业"),
//     new Array("c341","锅炉及原动设备制造"),
//     new Array("c342","金属加工机械制造"),
//     new Array("c343","物料搬运设备制造"),
//     new Array("c344","泵、阀门、压缩机及类似机械制造"),
//     new Array("c361","汽车整车制造"),
//     new Array("c362","改装汽车制造"),
//     new Array("c363","低速载货汽车制造"),
//     new Array("c364","电车制造"),
//     new Array("c365","汽车车身、挂车制造"),
//     new Array("c366","汽车零部件及配件制造"),
//     new Array("c371","铁路运输设备制造"),
//     new Array("c372","城市轨道交通设备制造"),
//     new Array("c373","船舶及相关装置制造"),
//     new Array("c374","航空、航天器及设备制造"),
//     new Array("c375","摩托车制造"),
//     new Array("c376","自行车制造"),
//     new Array("c377","非公路休闲车及零配件制造"),
//     new Array("c379","潜水救捞及其他未列明运输设备制造"),
//     new Array("c381","电机制造"),
//     new Array("c382","输配电及控制设备制造"),
//     new Array("c383","电线、电缆、光缆及电工器材制造"),
//     new Array("c384","电池制造"),
//     new Array("c385","家用电力器具制造"),
//     new Array("c386","非电力家用器具制造"),
//     new Array("c387","照明器具制造"),
//     new Array("c389","其他电气机械及器材制造"),
//     new Array("c391","计算机制造"),
//     new Array("c392","通信设备制造"),
//     new Array("c393","广播电视设备制造"),
//     new Array("c394","雷达及配套设备制造"),
//     new Array("c395","视听设备制造"),
//     new Array("c396","电子器件制造"),
//     new Array("c397","电子元件制造"),
//     new Array("c399","其他电子设备制造"),
//     new Array("c401","通用仪器仪表制造"),
//     new Array("c402","专用仪器仪表制造"),
//     new Array("c403","钟表与计时仪器制造"),
//     new Array("c404","光学仪器及眼镜制造"),
//     new Array("c409","其他仪器仪表制造业"),
//     new Array("c411","日用杂品制造"),
//     new Array("c412","煤制品制造"),
//     new Array("c413","核辐射加工"),
//     new Array("c419","其他未列明制造业"),
//     new Array("c421","金属废料和碎屑加工处理"),
//     new Array("c422","非金属废料和碎屑加工处理"),
//     new Array("c431","金属制品修理"),
//     new Array("c432","通用设备修理"),
//     new Array("c433","专用设备修理"),
//     new Array("c434","铁路、船舶、航空航天等运输设备修理"),
//     new Array("c435","电气设备修理"),
//     new Array("c436","仪器仪表修理"),
//     new Array("c439","其他机械和设备修理业"),
//     new Array("f528","五金、家具及室内装饰材料专门零售"),
//     new Array("f529","货摊、无店铺及其他零售业"),
//     new Array("f521","综合零售"),
//     new Array("f522","食品、饮料及烟草制品专门零售"),
//     new Array("f523","纺织、服装及日用品专门零售"),
//     new Array("f524","文化、体育用品及器材专门零售"),
//     new Array("f525","医药及医疗器材专门零售"),
//     new Array("f526","汽车、摩托车、燃料及零配件专门零售"),
//     new Array("f527","家用电器及电子产品专门零售"),
//     new Array("f517","机械设备、五金产品及电子产品批发"),
//     new Array("f518","贸易经纪与代理"),
//     new Array("f519","其他批发业"),
//     new Array("f511","农、林、牧产品批发"),
//     new Array("f512","食品、饮料及烟草制品批发"),
//     new Array("f513","纺织、服装及家庭用品批发"),
//     new Array("f514","文化、体育用品及器材批发"),
//     new Array("f515","医药及医疗器材批发"),
//     new Array("f516","矿产品、建材及化工产品批发"),
//     new Array("h611","旅游饭店"),
//     new Array("h612","一般旅馆"),
//     new Array("h619","其他住宿业"),
//     new Array("h621","正餐服务"),
//     new Array("h622","快餐服务"),
//     new Array("h623","饮料及冷饮服务"),
//     new Array("h629","其他餐饮业"),
//     new Array("a011","谷物种植"),
//     new Array("a012","豆类、油料和薯类种植"),
//     new Array("a013","棉、麻、糖、烟草种植"),
//     new Array("a014","蔬菜、食用菌及园艺作物种植"),
//     new Array("a015","水果种植"),
//     new Array("a016","坚果、含油果、香料和饮料作物种植"),
//     new Array("a017","中药材种植"),
//     new Array("a019","其他农业"),
//     new Array("a021","林木育种和育苗"),
//     new Array("a022","造林和更新"),
//     new Array("a023","森林经营和管护"),
//     new Array("a024","木材和竹材采运"),
//     new Array("a025","林产品采集"),
//     new Array("a031","牲畜饲养"),
//     new Array("a032","家禽饲养"),
//     new Array("a033","狩猎和捕捉动物"),
//     new Array("a039","其他畜牧业"),
//     new Array("a041","水产养殖"),
//     new Array("a042","水产捕捞"),
//     new Array("a052","林业服务业"),
//     new Array("a054","渔业服务业"),
//     new Array("a051","农业服务业"),
//     new Array("a053","畜牧服务业"),
//     new Array("t960","国际组织"),
//     new Array("b061","烟煤和无烟煤开采洗选"),
//     new Array("b062","褐煤开采洗选"),
//     new Array("b069","其他煤炭采选"),
//     new Array("b071","石油开采"),
//     new Array("b072","天然气开采"),
//     new Array("b082","锰矿、铬矿采选"),
//     new Array("b089","其他黑色金属矿采选"),
//     new Array("b081","铁矿采选"),
//     new Array("b091","常用有色金属矿采选"),
//     new Array("b092","贵金属矿采选"),
//     new Array("b093","稀有稀土金属矿采选"),
//     new Array("b101","土砂石开采"),
//     new Array("b102","化学矿开采"),
//     new Array("b103","采盐"),
//     new Array("b109","石棉及其他非金属矿采选"),
//     new Array("b111","煤炭开采和洗选辅助活动"),
//     new Array("b112","石油和天然气开采辅助活动"),
//     new Array("b119","其他开采辅助活动"),
//     new Array("b120","其他采矿业")
// );
//
// var industryCode3 = {};
//
// for(var i = 0; i<code3.length;i++){industryCode3[code3[i][0]]={ids:code3[i][0],name:code3[i][1]};};
//
// var code4 = new Array(
//     new Array("q8311","综合医院"),
//     new Array("q8312","中医医院"),
//     new Array("q8313","中西医结合医院"),
//     new Array("q8314","民族医院"),
//     new Array("q8315","专科医院"),
//     new Array("q8316","疗养院"),
//     new Array("q8321","社区卫生服务中心（站）"),
//     new Array("q8322","街道卫生院"),
//     new Array("q8323","乡镇卫生院"),
//     new Array("q8330","门诊部（所）"),
//     new Array("q8340","计划生育技术服务活动"),
//     new Array("q8350","妇幼保健院（所、站）"),
//     new Array("q8360","专科疾病防治院（所、站）"),
//     new Array("q8370","疾病预防控制中心"),
//     new Array("q8390","其他卫生活动"),
//     new Array("q8411","干部休养所"),
//     new Array("q8412","护理机构服务"),
//     new Array("q8413","精神康复服务"),
//     new Array("q8414","老年人、残疾人养护服务"),
//     new Array("q8415","孤残儿童收养和庇护服务"),
//     new Array("q8419","其他提供住宿社会救助"),
//     new Array("q8421","社会看护与帮助服务"),
//     new Array("q8429","其他不提供住宿社会工作"),
//     new Array("r8510","新闻业"),
//     new Array("r8524","音像制品出版"),
//     new Array("r8525","电子出版物出版"),
//     new Array("r8529","其他出版业"),
//     new Array("r8521","图书出版"),
//     new Array("r8522","报纸出版"),
//     new Array("r8523","期刊出版"),
//     new Array("r8610","广播"),
//     new Array("r8620","电视"),
//     new Array("r8630","电影和影视节目制作"),
//     new Array("r8640","电影和影视节目发行"),
//     new Array("r8650","电影放映"),
//     new Array("r8660","录音制作"),
//     new Array("r8710","文艺创作与表演"),
//     new Array("r8720","艺术表演场馆"),
//     new Array("r8731","图书馆"),
//     new Array("r8732","档案馆"),
//     new Array("r8740","文物及非物质文化遗产保护"),
//     new Array("r8750","博物馆"),
//     new Array("r8760","烈士陵园、纪念馆"),
//     new Array("r8770","群众文化活动"),
//     new Array("r8790","其他文化艺术业"),
//     new Array("r8810","体育组织"),
//     new Array("r8820","体育场馆"),
//     new Array("r8830","休闲健身活动"),
//     new Array("r8890","其他体育"),
//     new Array("r8911","歌舞厅娱乐活动"),
//     new Array("r8912","电子游艺厅娱乐活动"),
//     new Array("r8913","网吧活动"),
//     new Array("r8919","其他室内娱乐活动"),
//     new Array("r8920","游乐园"),
//     new Array("r8930","彩票活动"),
//     new Array("r8941","文化娱乐经纪人"),
//     new Array("r8942","体育经纪人"),
//     new Array("r8949","其他文化艺术经纪代理"),
//     new Array("r8990","其他娱乐业"),
//     new Array("k7010","房地产开发经营"),
//     new Array("k7020","物业管理"),
//     new Array("k7030","房地产中介服务"),
//     new Array("k7040","自有房地产经营活动"),
//     new Array("k7090","其他房地产业"),
//     new Array("j6811","人寿保险"),
//     new Array("j6812","健康和意外保险"),
//     new Array("j6820","财产保险"),
//     new Array("j6830","再保险"),
//     new Array("j6840","养老金"),
//     new Array("j6850","保险经纪与代理服务"),
//     new Array("j6860","保险监管服务"),
//     new Array("j6891","风险和损失评估"),
//     new Array("j6899","其他未列明保险活动"),
//     new Array("j6610","中央银行服务"),
//     new Array("j6620","货币银行服务"),
//     new Array("j6631","金融租赁服务"),
//     new Array("j6632","财务公司"),
//     new Array("j6633","典当"),
//     new Array("j6639","其他非货币银行服务"),
//     new Array("j6640","银行监管服务"),
//     new Array("j6721","期货市场管理服务"),
//     new Array("j6729","其他期货市场服务"),
//     new Array("j6730","证券期货监管服务"),
//     new Array("j6740","资本投资服务"),
//     new Array("j6790","其他资本市场服务"),
//     new Array("j6711","证券市场管理服务"),
//     new Array("j6712","证券经纪交易服务"),
//     new Array("j6713","基金管理服务"),
//     new Array("j6910","金融信托与管理服务"),
//     new Array("j6920","控股公司服务"),
//     new Array("j6930","非金融机构支付服务"),
//     new Array("j6940","金融信息服务"),
//     new Array("j6990","其他未列明金融业"),
//     new Array("l7111","汽车租赁"),
//     new Array("l7112","农业机械租赁"),
//     new Array("l7113","建筑工程机械与设备租赁"),
//     new Array("l7114","计算机及通讯设备租赁"),
//     new Array("l7119","其他机械与设备租赁"),
//     new Array("l7121","娱乐及体育设备出租"),
//     new Array("l7122","图书出租"),
//     new Array("l7123","音像制品出租"),
//     new Array("l7129","其他文化及日用品出租"),
//     new Array("l7211","企业总部管理"),
//     new Array("l7212","投资与资产管理"),
//     new Array("l7213","单位后勤管理服务"),
//     new Array("l7219","其他企业管理服务"),
//     new Array("l7221","律师及相关法律服务"),
//     new Array("l7222","公证服务"),
//     new Array("l7229","其他法律服务"),
//     new Array("l7231","会计、审计及税务服务"),
//     new Array("l7232","市场调查"),
//     new Array("l7233","社会经济咨询"),
//     new Array("l7239","其他专业咨询"),
//     new Array("l7240","广告业"),
//     new Array("l7250","知识产权服务"),
//     new Array("l7261","公共就业服务"),
//     new Array("l7262","职业中介服务"),
//     new Array("l7263","劳务派遣服务"),
//     new Array("l7269","其他人力资源服务"),
//     new Array("l7271","旅行社服务"),
//     new Array("l7272","旅游管理服务"),
//     new Array("l7279","其他旅行社相关服务"),
//     new Array("l7281","安全服务"),
//     new Array("l7282","安全系统监控服务"),
//     new Array("l7289","其他安全保护服务"),
//     new Array("l7291","市场管理"),
//     new Array("l7292","会议及展览服务"),
//     new Array("l7293","包装服务"),
//     new Array("l7294","办公服务"),
//     new Array("l7295","信用服务"),
//     new Array("l7296","担保服务"),
//     new Array("l7299","其他未列明商务服务业"),
//     new Array("e5010","建筑装饰业"),
//     new Array("e5021","建筑物拆除活动"),
//     new Array("e5029","其他工程准备活动"),
//     new Array("e5030","提供施工设备服务"),
//     new Array("e5090","其他未列明建筑业"),
//     new Array("e4700","房屋建筑业"),
//     new Array("e4811","铁路工程建筑"),
//     new Array("e4812","公路工程建筑"),
//     new Array("e4813","市政道路工程建筑"),
//     new Array("e4819","其他道路、隧道和桥梁工程建筑"),
//     new Array("e4821","水源及供水设施工程建筑"),
//     new Array("e4822","河湖治理及防洪设施工程建筑"),
//     new Array("e4823","港口及航运设施工程建筑"),
//     new Array("e4830","海洋工程建筑"),
//     new Array("e4840","工矿工程建筑"),
//     new Array("e4851","架线及设备工程建筑"),
//     new Array("e4852","管道工程建筑"),
//     new Array("e4890","其他土木工程建筑"),
//     new Array("e4910","电气安装"),
//     new Array("e4920","管道和设备安装"),
//     new Array("e4990","其他建筑安装业"),
//     new Array("s9000","中国共产党机关"),
//     new Array("s9110","国家权力机构"),
//     new Array("s9121","综合事务管理机构"),
//     new Array("s9122","对外事务管理机构"),
//     new Array("s9123","公共安全管理机构"),
//     new Array("s9124","社会事务管理机构"),
//     new Array("s9125","经济事务管理机构"),
//     new Array("s9126","行政监督检查机构"),
//     new Array("s9131","人民法院"),
//     new Array("s9132","人民检察院"),
//     new Array("s9190","其他国家机构"),
//     new Array("s9210","人民政协"),
//     new Array("s9220","民主党派"),
//     new Array("s9300","社会保障"),
//     new Array("s9411","工会"),
//     new Array("s9412","妇联"),
//     new Array("s9413","共青团"),
//     new Array("s9419","其他群众团体"),
//     new Array("s9421","专业性团体"),
//     new Array("s9422","行业性团体"),
//     new Array("s9429","其他社会团体"),
//     new Array("s9430","基金会"),
//     new Array("s9440","宗教组织"),
//     new Array("s9510","社区自治组织"),
//     new Array("s9520","村民自治组织"),
//     new Array("p8210","学前教育"),
//     new Array("p8221","普通小学教育"),
//     new Array("p8222","成人小学教育"),
//     new Array("p8231","普通初中教育"),
//     new Array("p8232","职业初中教育"),
//     new Array("p8233","成人初中教育"),
//     new Array("p8234","普通高中教育"),
//     new Array("p8235","成人高中教育"),
//     new Array("p8236","中等职业学校教育"),
//     new Array("p8241","普通高等教育"),
//     new Array("p8242","成人高等教育"),
//     new Array("p8250","特殊教育"),
//     new Array("p8291","职业技能培训"),
//     new Array("p8292","体校及体育培训"),
//     new Array("p8293","文化艺术培训"),
//     new Array("p8294","教育辅助服务"),
//     new Array("p8299","其他未列明教育"),
//     new Array("o7920","托儿所服务"),
//     new Array("o7930","洗染服务"),
//     new Array("o7940","理发及美容服务"),
//     new Array("o7950","洗浴服务"),
//     new Array("o7960","保健服务"),
//     new Array("o7970","婚姻服务"),
//     new Array("o7980","殡葬服务"),
//     new Array("o7990","其他居民服务业"),
//     new Array("o7910","家庭服务"),
//     new Array("o8011","汽车修理与维护"),
//     new Array("o8012","摩托车修理与维护"),
//     new Array("o8021","计算机和辅助设备修理"),
//     new Array("o8022","通讯设备修理"),
//     new Array("o8029","其他办公设备维修"),
//     new Array("o8031","家用电子产品修理"),
//     new Array("o8032","日用电器修理"),
//     new Array("o8091","自行车修理"),
//     new Array("o8092","鞋和皮革修理"),
//     new Array("o8093","家具和相关物品修理"),
//     new Array("o8099","其他未列明日用产品修理业"),
//     new Array("o8111","建筑物清洁服务"),
//     new Array("o8119","其他清洁服务"),
//     new Array("o8190","其他未列明服务业"),
//     new Array("d4411","火力发电"),
//     new Array("d4412","水力发电"),
//     new Array("d4413","核力发电"),
//     new Array("d4414","风力发电"),
//     new Array("d4415","太阳能发电"),
//     new Array("d4419","其他电力生产"),
//     new Array("d4420","电力供应"),
//     new Array("d4430","热力生产和供应"),
//     new Array("d4500","燃气生产和供应业"),
//     new Array("d4610","自来水生产和供应"),
//     new Array("d4620","污水处理及其再生利用"),
//     new Array("d4690","其他水的处理、利用与分配"),
//     new Array("i6311","固定电信服务"),
//     new Array("i6312","移动电信服务"),
//     new Array("i6319","其他电信服务"),
//     new Array("i6321","有线广播电视传输服务"),
//     new Array("i6322","无线广播电视传输服务"),
//     new Array("i6330","卫星传输服务"),
//     new Array("i6410","互联网接入及相关服务"),
//     new Array("i6420","互联网信息服务"),
//     new Array("i6490","其他互联网服务"),
//     new Array("i6540","数据处理和存储服务"),
//     new Array("i6550","集成电路设计"),
//     new Array("i6591","数字内容服务"),
//     new Array("i6592","呼叫中心"),
//     new Array("i6599","其他未列明信息技术服务业"),
//     new Array("i6510","软件开发"),
//     new Array("i6520","信息系统集成服务"),
//     new Array("i6530","信息技术咨询服务"),
//     new Array("n7711","自然保护区管理"),
//     new Array("n7712","野生动物保护"),
//     new Array("n7713","野生植物保护"),
//     new Array("n7719","其他自然保护"),
//     new Array("n7721","水污染治理"),
//     new Array("n7722","大气污染治理"),
//     new Array("n7723","固体废物治理"),
//     new Array("n7724","危险废物治理"),
//     new Array("n7725","放射性废物治理"),
//     new Array("n7729","其他污染治理"),
//     new Array("n7810","市政设施管理"),
//     new Array("n7820","环境卫生管理"),
//     new Array("n7830","城乡市容管理"),
//     new Array("n7840","绿化管理"),
//     new Array("n7851","公园管理"),
//     new Array("n7852","游览景区管理"),
//     new Array("n7610","防洪除涝设施管理"),
//     new Array("n7620","水资源管理"),
//     new Array("n7630","天然水收集与分配"),
//     new Array("n7640","水文服务"),
//     new Array("n7690","其他水利管理业"),
//     new Array("g5513","客运轮渡运输"),
//     new Array("g5511","海洋旅客运输"),
//     new Array("g5512","内河旅客运输"),
//     new Array("g5521","远洋货物运输"),
//     new Array("g5522","沿海货物运输"),
//     new Array("g5523","内河货物运输"),
//     new Array("g5531","客运港口"),
//     new Array("g5532","货运港口"),
//     new Array("g5539","其他水上运输辅助活动"),
//     new Array("g5310","铁路旅客运输"),
//     new Array("g5320","铁路货物运输"),
//     new Array("g5331","客运火车站"),
//     new Array("g5332","货运火车站"),
//     new Array("g5339","其他铁路运输辅助活动"),
//     new Array("g5420","公路旅客运输"),
//     new Array("g5430","道路货物运输"),
//     new Array("g5441","客运汽车站"),
//     new Array("g5442","公路管理与养护"),
//     new Array("g5449","其他道路运输辅助活动"),
//     new Array("g5413","出租车客运"),
//     new Array("g5419","其他城市公共交通运输"),
//     new Array("g5411","公共电汽车客运"),
//     new Array("g5412","城市轨道交通"),
//     new Array("g5611","航空旅客运输"),
//     new Array("g5612","航空货物运输"),
//     new Array("g5620","通用航空服务"),
//     new Array("g5631","机场"),
//     new Array("g5632","空中交通管理"),
//     new Array("g5639","其他航空运输辅助活动"),
//     new Array("g5700","管道运输业"),
//     new Array("g5810","装卸搬运"),
//     new Array("g5821","货物运输代理"),
//     new Array("g5822","旅客票务代理"),
//     new Array("g5829","其他运输代理业"),
//     new Array("g5911","谷物仓储"),
//     new Array("g5912","棉花仓储"),
//     new Array("g5919","其他农产品仓储"),
//     new Array("g5990","其他仓储业"),
//     new Array("g6010","邮政基本服务"),
//     new Array("g6020","快递服务"),
//     new Array("m7310","自然科学研究和试验发展"),
//     new Array("m7320","工程和技术研究和试验发展"),
//     new Array("m7330","农业科学研究和试验发展"),
//     new Array("m7340","医学研究和试验发展"),
//     new Array("m7350","社会人文科学研究"),
//     new Array("m7410","气象服务"),
//     new Array("m7420","地震服务"),
//     new Array("m7430","海洋服务"),
//     new Array("m7440","测绘服务"),
//     new Array("m7450","质检技术服务"),
//     new Array("m7461","环境保护监测"),
//     new Array("m7462","生态监测"),
//     new Array("m7471","能源矿产地质勘查"),
//     new Array("m7472","固体矿产地质勘查"),
//     new Array("m7473","水、二氧化碳等矿产地质勘查"),
//     new Array("m7474","基础地质勘查"),
//     new Array("m7475","地质勘查技术服务"),
//     new Array("m7481","工程管理服务"),
//     new Array("m7482","工程勘察设计"),
//     new Array("m7483","规划管理"),
//     new Array("m7491","专业化设计服务"),
//     new Array("m7492","摄影扩印服务"),
//     new Array("m7493","兽医服务"),
//     new Array("m7499","其他未列明专业技术服务业"),
//     new Array("m7511","农业技术推广服务"),
//     new Array("m7512","生物技术推广服务"),
//     new Array("m7513","新材料技术推广服务"),
//     new Array("m7514","节能技术推广服务"),
//     new Array("m7519","其他技术推广服务"),
//     new Array("m7520","科技中介服务"),
//     new Array("m7590","其他科技推广和应用服务业"),
//     new Array("c1310","谷物磨制"),
//     new Array("c1320","饲料加工"),
//     new Array("c1331","食用植物油加工"),
//     new Array("c1332","非食用植物油加工"),
//     new Array("c1340","制糖业"),
//     new Array("c1351","牲畜屠宰"),
//     new Array("c1352","禽类屠宰"),
//     new Array("c1353","肉制品及副产品加工"),
//     new Array("c1361","水产品冷冻加工"),
//     new Array("c1362","鱼糜制品及水产品干腌制加工"),
//     new Array("c1363","水产饲料制造"),
//     new Array("c1364","鱼油提取及制品制造"),
//     new Array("c1369","其他水产品加工"),
//     new Array("c1371","蔬菜加工"),
//     new Array("c1372","水果和坚果加工"),
//     new Array("c1391","淀粉及淀粉制品制造"),
//     new Array("c1392","豆制品制造"),
//     new Array("c1393","蛋品加工"),
//     new Array("c1399","其他未列明农副食品加工"),
//     new Array("c1411","糕点、面包制造"),
//     new Array("c1419","饼干及其他焙烤食品制造"),
//     new Array("c1421","糖果、巧克力制造"),
//     new Array("c1422","蜜饯制作"),
//     new Array("c1431","米、面制品制造"),
//     new Array("c1432","速冻食品制造"),
//     new Array("c1439","方便面及其他方便食品制造"),
//     new Array("c1440","乳制品制造"),
//     new Array("c1451","肉、禽类罐头制造"),
//     new Array("c1452","水产品罐头制造"),
//     new Array("c1453","蔬菜、水果罐头制造"),
//     new Array("c1459","其他罐头食品制造"),
//     new Array("c1461","味精制造"),
//     new Array("c1462","酱油、食醋及类似制品制造"),
//     new Array("c1469","其他调味品、发酵制品制造"),
//     new Array("c1491","营养食品制造"),
//     new Array("c1492","保健食品制造"),
//     new Array("c1494","盐加工"),
//     new Array("c1495","食品及饲料添加剂制造"),
//     new Array("c1499","其他未列明食品制造"),
//     new Array("c1493","冷冻饮品及食用冰制造"),
//     new Array("c1521","碳酸饮料制造"),
//     new Array("c1522","瓶（罐）装饮用水制造"),
//     new Array("c1523","果菜汁及果菜汁饮料制造"),
//     new Array("c1524","含乳饮料和植物蛋白饮料制造"),
//     new Array("c1525","固体饮料制造"),
//     new Array("c1529","茶饮料及其他饮料制造"),
//     new Array("c1513","啤酒制造"),
//     new Array("c1514","黄酒制造"),
//     new Array("c1515","葡萄酒制造"),
//     new Array("c1519","其他酒制造"),
//     new Array("c1511","酒精制造"),
//     new Array("c1512","白酒制造"),
//     new Array("c1530","精制茶加工"),
//     new Array("c1610","烟叶复烤"),
//     new Array("c1620","卷烟制造"),
//     new Array("c1690","其他烟草制品制造"),
//     new Array("c1711","棉纺纱加工"),
//     new Array("c1712","棉织造加工"),
//     new Array("c1713","棉印染精加工"),
//     new Array("c1721","毛条和毛纱线加工"),
//     new Array("c1722","毛织造加工"),
//     new Array("c1723","毛染整精加工"),
//     new Array("c1731","麻纤维纺前加工和纺纱"),
//     new Array("c1732","麻织造加工"),
//     new Array("c1733","麻染整精加工"),
//     new Array("c1741","缫丝加工"),
//     new Array("c1742","绢纺和丝织加工"),
//     new Array("c1743","丝印染精加工"),
//     new Array("c1751","化纤织造加工"),
//     new Array("c1752","化纤织物染整精加工"),
//     new Array("c1761","针织或钩针编织物织造"),
//     new Array("c1762","针织或钩针编织物印染精加工"),
//     new Array("c1763","针织或钩针编织品制造"),
//     new Array("c1771","床上用品制造"),
//     new Array("c1772","毛巾类制品制造"),
//     new Array("c1773","窗帘、布艺类产品制造"),
//     new Array("c1779","其他家用纺织制成品制造"),
//     new Array("c1781","非织造布制造"),
//     new Array("c1782","绳、索、缆制造"),
//     new Array("c1783","纺织带和帘子布制造"),
//     new Array("c1784","篷、帆布制造"),
//     new Array("c1789","其他非家用纺织制成品制造"),
//     new Array("c1810","机织服装制造"),
//     new Array("c1820","针织或钩针编织服装制造"),
//     new Array("c1830","服饰制造"),
//     new Array("c1910","皮革鞣制加工"),
//     new Array("c1921","皮革服装制造"),
//     new Array("c1922","皮箱、包（袋）制造"),
//     new Array("c1923","皮手套及皮装饰制品制造"),
//     new Array("c1929","其他皮革制品制造"),
//     new Array("c1931","毛皮鞣制加工"),
//     new Array("c1932","毛皮服装加工"),
//     new Array("c1939","其他毛皮制品加工"),
//     new Array("c1941","羽毛（绒）加工"),
//     new Array("c1942","羽毛（绒）制品加工"),
//     new Array("c1951","纺织面料鞋制造"),
//     new Array("c1952","皮鞋制造"),
//     new Array("c1953","塑料鞋制造"),
//     new Array("c1954","橡胶鞋制造"),
//     new Array("c1959","其他制鞋业"),
//     new Array("c2011","锯材加工"),
//     new Array("c2012","木片加工"),
//     new Array("c2013","单板加工"),
//     new Array("c2019","其他木材加工"),
//     new Array("c2021","胶合板制造"),
//     new Array("c2022","纤维板制造"),
//     new Array("c2023","刨花板制造"),
//     new Array("c2029","其他人造板制造"),
//     new Array("c2041","竹制品制造"),
//     new Array("c2042","藤制品制造"),
//     new Array("c2043","棕制品制造"),
//     new Array("c2049","草及其他制品制造"),
//     new Array("c2031","建筑用木料及木材组件加工"),
//     new Array("c2032","木门窗、楼梯制造"),
//     new Array("c2033","地板制造"),
//     new Array("c2034","木制容器制造"),
//     new Array("c2039","软木制品及其他木制品制造"),
//     new Array("c2110","木质家具制造"),
//     new Array("c2120","竹、藤家具制造"),
//     new Array("c2130","金属家具制造"),
//     new Array("c2140","塑料家具制造"),
//     new Array("c2190","其他家具制造"),
//     new Array("c2211","木竹浆制造"),
//     new Array("c2212","非木竹浆制造"),
//     new Array("c2221","机制纸及纸板制造"),
//     new Array("c2222","手工纸制造"),
//     new Array("c2223","加工纸制造"),
//     new Array("c2231","纸和纸板容器制造"),
//     new Array("c2239","其他纸制品制造"),
//     new Array("c2311","书、报刊印刷"),
//     new Array("c2312","本册印制"),
//     new Array("c2319","包装装潢及其他印刷"),
//     new Array("c2320","装订及印刷相关服务"),
//     new Array("c2330","记录媒介复制"),
//     new Array("c2411","文具制造"),
//     new Array("c2412","笔的制造"),
//     new Array("c2413","教学用模型及教具制造"),
//     new Array("c2414","墨水、墨汁制造"),
//     new Array("c2419","其他文教办公用品制造"),
//     new Array("c2421","中乐器制造"),
//     new Array("c2422","西乐器制造"),
//     new Array("c2423","电子乐器制造"),
//     new Array("c2429","其他乐器及零件制造"),
//     new Array("c2431","雕塑工艺品制造"),
//     new Array("c2432","金属工艺品制造"),
//     new Array("c2433","漆器工艺品制造"),
//     new Array("c2434","花画工艺品制造"),
//     new Array("c2435","天然植物纤维编织工艺品制造"),
//     new Array("c2436","抽纱刺绣工艺品制造"),
//     new Array("c2437","地毯、挂毯制造"),
//     new Array("c2438","珠宝首饰及有关物品制造"),
//     new Array("c2439","其他工艺美术品制造"),
//     new Array("c2441","球类制造"),
//     new Array("c2442","体育器材及配件制造"),
//     new Array("c2443","训练健身器材制造"),
//     new Array("c2444","运动防护用具制造"),
//     new Array("c2449","其他体育用品制造"),
//     new Array("c2450","玩具制造"),
//     new Array("c2461","露天游乐场所游乐设备制造"),
//     new Array("c2462","游艺用品及室内游艺器材制造"),
//     new Array("c2469","其他娱乐用品制造"),
//     new Array("c2511","原油加工及石油制品制造"),
//     new Array("c2512","人造原油制造"),
//     new Array("c2520","炼焦"),
//     new Array("c2530","核燃料加工"),
//     new Array("c2611","无机酸制造"),
//     new Array("c2612","无机碱制造"),
//     new Array("c2613","无机盐制造"),
//     new Array("c2614","有机化学原料制造"),
//     new Array("c2619","其他基础化学原料制造"),
//     new Array("c2625","有机肥料及微生物肥料制造"),
//     new Array("c2629","其他肥料制造"),
//     new Array("c2621","氮肥制造"),
//     new Array("c2622","磷肥制造"),
//     new Array("c2623","钾肥制造"),
//     new Array("c2624","复混肥料制造"),
//     new Array("c2631","化学农药制造"),
//     new Array("c2632","生物化学农药及微生物农药制造"),
//     new Array("c2641","涂料制造"),
//     new Array("c2642","油墨及类似产品制造"),
//     new Array("c2643","颜料制造"),
//     new Array("c2644","染料制造"),
//     new Array("c2645","密封用填料及类似品制造"),
//     new Array("c2651","初级形态塑料及合成树脂制造"),
//     new Array("c2652","合成橡胶制造"),
//     new Array("c2653","合成纤维单（聚合）体制造"),
//     new Array("c2659","其他合成材料制造"),
//     new Array("c2661","化学试剂和助剂制造"),
//     new Array("c2662","专项化学用品制造"),
//     new Array("c2663","林产化学产品制造"),
//     new Array("c2664","信息化学品制造"),
//     new Array("c2665","环境污染处理专用药剂材料制造"),
//     new Array("c2666","动物胶制造"),
//     new Array("c2669","其他专用化学产品制造"),
//     new Array("c2671","炸药及火工产品制造"),
//     new Array("c2672","焰火、鞭炮产品制造"),
//     new Array("c2681","肥皂及合成洗涤剂制造"),
//     new Array("c2682","化妆品制造"),
//     new Array("c2683","口腔清洁用品制造"),
//     new Array("c2684","香料、香精制造"),
//     new Array("c2689","其他日用化学产品制造"),
//     new Array("c2710","化学药品原料药制造"),
//     new Array("c2720","化学药品制剂制造"),
//     new Array("c2730","中药饮片加工"),
//     new Array("c2740","中成药生产"),
//     new Array("c2750","兽用药品制造"),
//     new Array("c2760","生物药品制造"),
//     new Array("c2770","卫生材料及医药用品制造"),
//     new Array("c2811","化纤浆粕制造"),
//     new Array("c2812","人造纤维（纤维素纤维）制造"),
//     new Array("c2821","锦纶纤维制造"),
//     new Array("c2822","涤纶纤维制造"),
//     new Array("c2823","腈纶纤维制造"),
//     new Array("c2824","维纶纤维制造"),
//     new Array("c2825","丙纶纤维制造"),
//     new Array("c2826","氨纶纤维制造"),
//     new Array("c2829","其他合成纤维制造"),
//     new Array("c2911","轮胎制造"),
//     new Array("c2912","橡胶板、管、带制造"),
//     new Array("c2913","橡胶零件制造"),
//     new Array("c2914","再生橡胶制造"),
//     new Array("c2915","日用及医用橡胶制品制造"),
//     new Array("c2919","其他橡胶制品制造"),
//     new Array("c2921","塑料薄膜制造"),
//     new Array("c2922","塑料板、管、型材制造"),
//     new Array("c2923","塑料丝、绳及编织品制造"),
//     new Array("c2924","泡沫塑料制造"),
//     new Array("c2925","塑料人造革、合成革制造"),
//     new Array("c2926","塑料包装箱及容器制造"),
//     new Array("c2927","日用塑料制品制造"),
//     new Array("c2928","塑料零件制造"),
//     new Array("c2929","其他塑料制品制造"),
//     new Array("c3011","水泥制造"),
//     new Array("c3012","石灰和石膏制造"),
//     new Array("c3021","水泥制品制造"),
//     new Array("c3022","砼结构构件制造"),
//     new Array("c3023","石棉水泥制品制造"),
//     new Array("c3024","轻质建筑材料制造"),
//     new Array("c3029","其他水泥类似制品制造"),
//     new Array("c3031","粘土砖瓦及建筑砌块制造"),
//     new Array("c3032","建筑陶瓷制品制造"),
//     new Array("c3033","建筑用石加工"),
//     new Array("c3034","防水建筑材料制造"),
//     new Array("c3035","隔热和隔音材料制造"),
//     new Array("c3039","其他建筑材料制造"),
//     new Array("c3041","平板玻璃制造"),
//     new Array("c3049","其他玻璃制造"),
//     new Array("c3051","技术玻璃制品制造"),
//     new Array("c3052","光学玻璃制造"),
//     new Array("c3053","玻璃仪器制造"),
//     new Array("c3054","日用玻璃制品制造"),
//     new Array("c3055","玻璃包装容器制造"),
//     new Array("c3056","玻璃保温容器制造"),
//     new Array("c3057","制镜及类似品加工"),
//     new Array("c3059","其他玻璃制品制造"),
//     new Array("c3061","玻璃纤维及制品制造"),
//     new Array("c3062","玻璃纤维增强塑料制品制造"),
//     new Array("c3071","卫生陶瓷制品制造"),
//     new Array("c3072","特种陶瓷制品制造"),
//     new Array("c3073","日用陶瓷制品制造"),
//     new Array("c3079","园林、陈设艺术及其他陶瓷制品制造"),
//     new Array("c3089","耐火陶瓷制品及其他耐火材料制造"),
//     new Array("c3081","石棉制品制造"),
//     new Array("c3082","云母制品制造"),
//     new Array("c3091","石墨及碳素制品制造"),
//     new Array("c3099","其他非金属矿物制品制造"),
//     new Array("c3140","钢压延加工"),
//     new Array("c3150","铁合金冶炼"),
//     new Array("c3110","炼铁"),
//     new Array("c3120","炼钢"),
//     new Array("c3130","黑色金属铸造"),
//     new Array("c3211","铜冶炼"),
//     new Array("c3212","铅锌冶炼"),
//     new Array("c3213","镍钴冶炼"),
//     new Array("c3214","锡冶炼"),
//     new Array("c3215","锑冶炼"),
//     new Array("c3216","铝冶炼"),
//     new Array("c3217","镁冶炼"),
//     new Array("c3219","其他常用有色金属冶炼"),
//     new Array("c3221","金冶炼"),
//     new Array("c3222","银冶炼"),
//     new Array("c3229","其他贵金属冶炼"),
//     new Array("c3231","钨钼冶炼"),
//     new Array("c3232","稀土金属冶炼"),
//     new Array("c3239","其他稀有金属冶炼"),
//     new Array("c3240","有色金属合金制造"),
//     new Array("c3250","有色金属铸造"),
//     new Array("c3261","铜压延加工"),
//     new Array("c3262","铝压延加工"),
//     new Array("c3263","贵金属压延加工"),
//     new Array("c3264","稀有稀土金属压延加工"),
//     new Array("c3269","其他有色金属压延加工"),
//     new Array("c3311","金属结构制造"),
//     new Array("c3312","金属门窗制造"),
//     new Array("c3321","切削工具制造"),
//     new Array("c3322","手工具制造"),
//     new Array("c3323","农用及园林用金属工具制造"),
//     new Array("c3324","刀剪及类似日用金属工具制造"),
//     new Array("c3329","其他金属工具制造"),
//     new Array("c3331","集装箱制造"),
//     new Array("c3332","金属压力容器制造"),
//     new Array("c3333","金属包装容器制造"),
//     new Array("c3340","金属丝绳及其制品制造"),
//     new Array("c3351","建筑、家具用金属配件制造"),
//     new Array("c3352","建筑装饰及水暖管道零件制造"),
//     new Array("c3353","安全、消防用金属制品制造"),
//     new Array("c3359","其他建筑、安全用金属制品制造"),
//     new Array("c3360","金属表面处理及热处理加工"),
//     new Array("c3371","生产专用搪瓷制品制造"),
//     new Array("c3372","建筑装饰搪瓷制品制造"),
//     new Array("c3373","搪瓷卫生洁具制造"),
//     new Array("c3379","搪瓷日用品及其他搪瓷制品制造"),
//     new Array("c3381","金属制厨房用器具制造"),
//     new Array("c3382","金属制餐具和器皿制造"),
//     new Array("c3383","金属制卫生器具制造"),
//     new Array("c3389","其他金属制日用品制造"),
//     new Array("c3391","锻件及粉末冶金制品制造"),
//     new Array("c3392","交通及公共管理用金属标牌制造"),
//     new Array("c3399","其他未列明金属制品制造"),
//     new Array("c3514","海洋工程专用设备制造"),
//     new Array("c3511","矿山机械制造"),
//     new Array("c3512","石油钻采专用设备制造"),
//     new Array("c3513","建筑工程用机械制造"),
//     new Array("c3515","建筑材料生产专用机械制造"),
//     new Array("c3516","冶金专用设备制造"),
//     new Array("c3521","炼油、化工生产专用设备制造"),
//     new Array("c3522","橡胶加工专用设备制造"),
//     new Array("c3523","塑料加工专用设备制造"),
//     new Array("c3524","木材加工机械制造"),
//     new Array("c3525","模具制造"),
//     new Array("c3529","其他非金属加工专用设备制造"),
//     new Array("c3531","食品、酒、饮料及茶生产专用设备制造"),
//     new Array("c3532","农副食品加工专用设备制造"),
//     new Array("c3533","烟草生产专用设备制造"),
//     new Array("c3534","饲料生产专用设备制造"),
//     new Array("c3541","制浆和造纸专用设备制造"),
//     new Array("c3542","印刷专用设备制造"),
//     new Array("c3543","日用化工专用设备制造"),
//     new Array("c3544","制药专用设备制造"),
//     new Array("c3545","照明器具生产专用设备制造"),
//     new Array("c3546","玻璃、陶瓷和搪瓷制品生产专用设备制造"),
//     new Array("c3549","其他日用品生产专用设备制造"),
//     new Array("c3551","纺织专用设备制造"),
//     new Array("c3552","皮革、毛皮及其制品加工专用设备制造"),
//     new Array("c3553","缝制机械制造"),
//     new Array("c3554","洗涤机械制造"),
//     new Array("c3561","电工机械专用设备制造"),
//     new Array("c3562","电子工业专用设备制造"),
//     new Array("c3574","畜牧机械制造"),
//     new Array("c3575","渔业机械制造"),
//     new Array("c3576","农林牧渔机械配件制造"),
//     new Array("c3577","棉花加工机械制造"),
//     new Array("c3579","其他农、林、牧、渔业机械制造"),
//     new Array("c3571","拖拉机制造"),
//     new Array("c3572","机械化农业及园艺机具制造"),
//     new Array("c3573","营林及木竹采伐机械制造"),
//     new Array("c3581","医疗诊断、监护及治疗设备制造"),
//     new Array("c3582","口腔科用设备及器具制造"),
//     new Array("c3583","医疗实验室及医用消毒设备和器具制造"),
//     new Array("c3584","医疗、外科及兽医用器械制造"),
//     new Array("c3585","机械治疗及病房护理设备制造"),
//     new Array("c3586","假肢、人工器官及植（介）入器械制造"),
//     new Array("c3589","其他医疗设备及器械制造"),
//     new Array("c3591","环境保护专用设备制造"),
//     new Array("c3592","地质勘查专用设备制造"),
//     new Array("c3593","邮政专用机械及器材制造"),
//     new Array("c3594","商业、饮食、服务专用设备制造"),
//     new Array("c3595","社会公共安全设备及器材制造"),
//     new Array("c3596","交通安全、管制及类似专用设备制造"),
//     new Array("c3597","水资源专用机械制造"),
//     new Array("c3599","其他专用设备制造"),
//     new Array("c3451","轴承制造"),
//     new Array("c3452","齿轮及齿轮减、变速箱制造"),
//     new Array("c3459","其他传动部件制造"),
//     new Array("c3461","烘炉、熔炉及电炉制造"),
//     new Array("c3462","风机、风扇制造"),
//     new Array("c3463","气体、液体分离及纯净设备制造"),
//     new Array("c3464","制冷、空调设备制造"),
//     new Array("c3465","风动和电动工具制造"),
//     new Array("c3466","喷枪及类似器具制造"),
//     new Array("c3467","衡器制造"),
//     new Array("c3468","包装专用设备制造"),
//     new Array("c3471","电影机械制造"),
//     new Array("c3472","幻灯及投影设备制造"),
//     new Array("c3473","照相机及器材制造"),
//     new Array("c3474","复印和胶印设备制造"),
//     new Array("c3475","计算器及货币专用设备制造"),
//     new Array("c3479","其他文化、办公用机械制造"),
//     new Array("c3481","金属密封件制造"),
//     new Array("c3482","紧固件制造"),
//     new Array("c3483","弹簧制造"),
//     new Array("c3484","机械零部件加工"),
//     new Array("c3489","其他通用零部件制造"),
//     new Array("c3490","其他通用设备制造业"),
//     new Array("c3411","锅炉及辅助设备制造"),
//     new Array("c3412","内燃机及配件制造"),
//     new Array("c3413","汽轮机及辅机制造"),
//     new Array("c3414","水轮机及辅机制造"),
//     new Array("c3415","风能原动设备制造"),
//     new Array("c3419","其他原动设备制造"),
//     new Array("c3421","金属切削机床制造"),
//     new Array("c3422","金属成形机床制造"),
//     new Array("c3423","铸造机械制造"),
//     new Array("c3424","金属切割及焊接设备制造"),
//     new Array("c3425","机床附件制造"),
//     new Array("c3429","其他金属加工机械制造"),
//     new Array("c3431","轻小型起重设备制造"),
//     new Array("c3432","起重机制造"),
//     new Array("c3433","生产专用车辆制造"),
//     new Array("c3434","连续搬运设备制造"),
//     new Array("c3435","电梯、自动扶梯及升降机制造"),
//     new Array("c3439","其他物料搬运设备制造"),
//     new Array("c3443","阀门和旋塞制造"),
//     new Array("c3444","液压和气压动力机械及元件制造"),
//     new Array("c3441","泵及真空设备制造"),
//     new Array("c3442","气体压缩机械制造"),
//     new Array("c3610","汽车整车制造"),
//     new Array("c3620","改装汽车制造"),
//     new Array("c3630","低速载货汽车制造"),
//     new Array("c3640","电车制造"),
//     new Array("c3650","汽车车身、挂车制造"),
//     new Array("c3660","汽车零部件及配件制造"),
//     new Array("c3711","铁路机车车辆及动车组制造"),
//     new Array("c3712","窄轨机车车辆制造"),
//     new Array("c3713","铁路机车车辆配件制造"),
//     new Array("c3714","铁路专用设备及器材、配件制造"),
//     new Array("c3719","其他铁路运输设备制造"),
//     new Array("c3720","城市轨道交通设备制造"),
//     new Array("c3735","船舶改装与拆除"),
//     new Array("c3739","航标器材及其他相关装置制造"),
//     new Array("c3731","金属船舶制造"),
//     new Array("c3732","非金属船舶制造"),
//     new Array("c3733","娱乐船和运动船制造"),
//     new Array("c3734","船用配套设备制造"),
//     new Array("c3741","飞机制造"),
//     new Array("c3742","航天器制造"),
//     new Array("c3743","航空、航天相关设备制造"),
//     new Array("c3749","其他航空航天器制造"),
//     new Array("c3751","摩托车整车制造"),
//     new Array("c3752","摩托车零部件及配件制造"),
//     new Array("c3761","脚踏自行车及残疾人座车制造"),
//     new Array("c3762","助动自行车制造"),
//     new Array("c3770","非公路休闲车及零配件制造"),
//     new Array("c3791","潜水及水下救捞装备制造"),
//     new Array("c3799","其他未列明运输设备制造"),
//     new Array("c3811","发电机及发电机组制造"),
//     new Array("c3812","电动机制造"),
//     new Array("c3819","微电机及其他电机制造"),
//     new Array("c3821","变压器、整流器和电感器制造"),
//     new Array("c3822","电容器及其配套设备制造"),
//     new Array("c3823","配电开关控制设备制造"),
//     new Array("c3824","电力电子元器件制造"),
//     new Array("c3825","光伏设备及元器件制造"),
//     new Array("c3829","其他输配电及控制设备制造"),
//     new Array("c3831","电线、电缆制造"),
//     new Array("c3832","光纤、光缆制造"),
//     new Array("c3833","绝缘制品制造"),
//     new Array("c3839","其他电工器材制造"),
//     new Array("c3841","锂离子电池制造"),
//     new Array("c3842","镍氢电池制造"),
//     new Array("c3849","其他电池制造"),
//     new Array("c3851","家用制冷电器具制造"),
//     new Array("c3852","家用空气调节器制造"),
//     new Array("c3853","家用通风电器具制造"),
//     new Array("c3854","家用厨房电器具制造"),
//     new Array("c3855","家用清洁卫生电器具制造"),
//     new Array("c3856","家用美容、保健电器具制造"),
//     new Array("c3857","家用电力器具专用配件制造"),
//     new Array("c3859","其他家用电力器具制造"),
//     new Array("c3861","燃气、太阳能及类似能源家用器具制造"),
//     new Array("c3869","其他非电力家用器具制造"),
//     new Array("c3871","电光源制造"),
//     new Array("c3872","照明灯具制造"),
//     new Array("c3879","灯用电器附件及其他照明器具制造"),
//     new Array("c3891","电气信号设备装置制造"),
//     new Array("c3899","其他未列明电气机械及器材制造"),
//     new Array("c3911","计算机整机制造"),
//     new Array("c3912","计算机零部件制造"),
//     new Array("c3913","计算机外围设备制造"),
//     new Array("c3919","其他计算机制造"),
//     new Array("c3921","通信系统设备制造"),
//     new Array("c3922","通信终端设备制造"),
//     new Array("c3939","应用电视设备及其他广播电视设备制造"),
//     new Array("c3931","广播电视节目制作及发射设备制造"),
//     new Array("c3932","广播电视接收设备及器材制造"),
//     new Array("c3940","雷达及配套设备制造"),
//     new Array("c3951","电视机制造"),
//     new Array("c3952","音响设备制造"),
//     new Array("c3953","影视录放设备制造"),
//     new Array("c3961","电子真空器件制造"),
//     new Array("c3962","半导体分立器件制造"),
//     new Array("c3963","集成电路制造"),
//     new Array("c3969","光电子器件及其他电子器件制造"),
//     new Array("c3971","电子元件及组件制造"),
//     new Array("c3972","印制电路板制造"),
//     new Array("c3990","其他电子设备制造"),
//     new Array("c4011","工业自动控制系统装置制造"),
//     new Array("c4012","电工仪器仪表制造"),
//     new Array("c4013","绘图、计算及测量仪器制造"),
//     new Array("c4014","实验分析仪器制造"),
//     new Array("c4015","试验机制造"),
//     new Array("c4019","供应用仪表及其他通用仪器制造"),
//     new Array("c4021","环境监测专用仪器仪表制造"),
//     new Array("c4022","运输设备及生产用计数仪表制造"),
//     new Array("c4023","导航、气象及海洋专用仪器制造"),
//     new Array("c4024","农林牧渔专用仪器仪表制造"),
//     new Array("c4025","地质勘探和地震专用仪器制造"),
//     new Array("c4026","教学专用仪器制造"),
//     new Array("c4027","核子及核辐射测量仪器制造"),
//     new Array("c4028","电子测量仪器制造"),
//     new Array("c4029","其他专用仪器制造"),
//     new Array("c4030","钟表与计时仪器制造"),
//     new Array("c4041","光学仪器制造"),
//     new Array("c4042","眼镜制造"),
//     new Array("c4090","其他仪器仪表制造业"),
//     new Array("c4111","鬃毛加工、制刷及清扫工具制造"),
//     new Array("c4119","其他日用杂品制造"),
//     new Array("c4120","煤制品制造"),
//     new Array("c4130","核辐射加工"),
//     new Array("c4190","其他未列明制造业"),
//     new Array("c4210","金属废料和碎屑加工处理"),
//     new Array("c4220","非金属废料和碎屑加工处理"),
//     new Array("c4310","金属制品修理"),
//     new Array("c4320","通用设备修理"),
//     new Array("c4330","专用设备修理"),
//     new Array("c4343","航空航天器修理"),
//     new Array("c4349","其他运输设备修理"),
//     new Array("c4342","船舶修理"),
//     new Array("c4341","铁路运输设备修理"),
//     new Array("c4350","电气设备修理"),
//     new Array("c4360","仪器仪表修理"),
//     new Array("c4390","其他机械和设备修理业"),
//     new Array("f5281","五金零售"),
//     new Array("f5282","灯具零售"),
//     new Array("f5283","家具零售"),
//     new Array("f5284","涂料零售"),
//     new Array("f5285","卫生洁具零售"),
//     new Array("f5286","木质装饰材料零售"),
//     new Array("f5287","陶瓷、石材装饰材料零售"),
//     new Array("f5289","其他室内装饰材料零售"),
//     new Array("f5291","货摊食品零售"),
//     new Array("f5292","货摊纺织、服装及鞋零售"),
//     new Array("f5293","货摊日用品零售"),
//     new Array("f5294","互联网零售"),
//     new Array("f5295","邮购及电视、电话零售"),
//     new Array("f5296","旧货零售"),
//     new Array("f5297","生活用燃料零售"),
//     new Array("f5299","其他未列明零售业"),
//     new Array("f5211","百货零售"),
//     new Array("f5212","超级市场零售"),
//     new Array("f5219","其他综合零售"),
//     new Array("f5221","粮油零售"),
//     new Array("f5222","糕点、面包零售"),
//     new Array("f5223","果品、蔬菜零售"),
//     new Array("f5224","肉、禽、蛋、奶及水产品零售"),
//     new Array("f5225","营养和保健品零售"),
//     new Array("f5226","酒、饮料及茶叶零售"),
//     new Array("f5227","烟草制品零售"),
//     new Array("f5229","其他食品零售"),
//     new Array("f5231","纺织品及针织品零售"),
//     new Array("f5232","服装零售"),
//     new Array("f5233","鞋帽零售"),
//     new Array("f5234","化妆品及卫生用品零售"),
//     new Array("f5235","钟表、眼镜零售"),
//     new Array("f5236","箱、包零售"),
//     new Array("f5237","厨房用具及日用杂品零售"),
//     new Array("f5238","自行车零售"),
//     new Array("f5239","其他日用品零售"),
//     new Array("f5242","体育用品及器材零售"),
//     new Array("f5243","图书、报刊零售"),
//     new Array("f5244","音像制品及电子出版物零售"),
//     new Array("f5245","珠宝首饰零售"),
//     new Array("f5246","工艺美术品及收藏品零售"),
//     new Array("f5247","乐器零售"),
//     new Array("f5248","照相器材零售"),
//     new Array("f5249","其他文化用品零售"),
//     new Array("f5241","文具用品零售"),
//     new Array("f5251","药品零售"),
//     new Array("f5252","医疗用品及器材零售"),
//     new Array("f5261","汽车零售"),
//     new Array("f5262","汽车零配件零售"),
//     new Array("f5263","摩托车及零配件零售"),
//     new Array("f5264","机动车燃料零售"),
//     new Array("f5271","家用视听设备零售"),
//     new Array("f5272","日用家电设备零售"),
//     new Array("f5273","计算机、软件及辅助设备零售"),
//     new Array("f5274","通信设备零售"),
//     new Array("f5279","其他电子产品零售"),
//     new Array("f5171","农业机械批发"),
//     new Array("f5172","汽车批发"),
//     new Array("f5173","汽车零配件批发"),
//     new Array("f5174","摩托车及零配件批发"),
//     new Array("f5175","五金产品批发"),
//     new Array("f5176","电气设备批发"),
//     new Array("f5177","计算机、软件及辅助设备批发"),
//     new Array("f5178","通讯及广播电视设备批发"),
//     new Array("f5179","其他机械设备及电子产品批发"),
//     new Array("f5181","贸易代理"),
//     new Array("f5182","拍卖"),
//     new Array("f5189","其他贸易经纪与代理"),
//     new Array("f5191","再生物资回收与批发"),
//     new Array("f5199","其他未列明批发业"),
//     new Array("f5111","谷物、豆及薯类批发"),
//     new Array("f5112","种子批发"),
//     new Array("f5113","饲料批发"),
//     new Array("f5114","棉、麻批发"),
//     new Array("f5115","林业产品批发"),
//     new Array("f5116","牲畜批发"),
//     new Array("f5119","其他农牧产品批发"),
//     new Array("f5121","米、面制品及食用油批发"),
//     new Array("f5122","糕点、糖果及糖批发"),
//     new Array("f5123","果品、蔬菜批发"),
//     new Array("f5124","肉、禽、蛋、奶及水产品批发"),
//     new Array("f5125","盐及调味品批发"),
//     new Array("f5126","营养和保健品批发"),
//     new Array("f5127","酒、饮料及茶叶批发"),
//     new Array("f5128","烟草制品批发"),
//     new Array("f5129","其他食品批发"),
//     new Array("f5131","纺织品、针织品及原料批发"),
//     new Array("f5132","服装批发"),
//     new Array("f5133","鞋帽批发"),
//     new Array("f5134","化妆品及卫生用品批发"),
//     new Array("f5135","厨房、卫生间用具及日用杂货批发"),
//     new Array("f5136","灯具、装饰物品批发"),
//     new Array("f5137","家用电器批发"),
//     new Array("f5139","其他家庭用品批发"),
//     new Array("f5141","文具用品批发"),
//     new Array("f5142","体育用品及器材批发"),
//     new Array("f5143","图书批发"),
//     new Array("f5144","报刊批发"),
//     new Array("f5145","音像制品及电子出版物批发"),
//     new Array("f5146","首饰、工艺品及收藏品批发"),
//     new Array("f5149","其他文化用品批发"),
//     new Array("f5151","西药批发"),
//     new Array("f5152","中药批发"),
//     new Array("f5153","医疗用品及器材批发"),
//     new Array("f5161","煤炭及制品批发"),
//     new Array("f5162","石油及制品批发"),
//     new Array("f5163","非金属矿及制品批发"),
//     new Array("f5164","金属及金属矿批发"),
//     new Array("f5165","建材批发"),
//     new Array("f5166","化肥批发"),
//     new Array("f5167","农药批发"),
//     new Array("f5168","农用薄膜批发"),
//     new Array("f5169","其他化工产品批发"),
//     new Array("h6110","旅游饭店"),
//     new Array("h6120","一般旅馆"),
//     new Array("h6190","其他住宿业"),
//     new Array("h6210","正餐服务"),
//     new Array("h6220","快餐服务"),
//     new Array("h6231","茶馆服务"),
//     new Array("h6232","咖啡馆服务"),
//     new Array("h6233","酒吧服务"),
//     new Array("h6239","其他饮料及冷饮服务"),
//     new Array("h6291","小吃服务"),
//     new Array("h6292","餐饮配送服务"),
//     new Array("h6299","其他未列明餐饮业"),
//     new Array("a0111","稻谷种植"),
//     new Array("a0112","小麦种植"),
//     new Array("a0113","玉米种植"),
//     new Array("a0119","其他谷物种植"),
//     new Array("a0121","豆类种植"),
//     new Array("a0122","油料种植"),
//     new Array("a0123","薯类种植"),
//     new Array("a0131","棉花种植"),
//     new Array("a0132","麻类种植"),
//     new Array("a0133","糖料种植"),
//     new Array("a0134","烟草种植"),
//     new Array("a0141","蔬菜种植"),
//     new Array("a0142","食用菌种植"),
//     new Array("a0143","花卉种植"),
//     new Array("a0149","其他园艺作物种植"),
//     new Array("a0151","仁果类和核果类水果种植"),
//     new Array("a0152","葡萄种植"),
//     new Array("a0153","柑橘类种植"),
//     new Array("a0154","香蕉等亚热带水果种植"),
//     new Array("a0159","其他水果种植"),
//     new Array("a0161","坚果种植"),
//     new Array("a0162","含油果种植"),
//     new Array("a0163","香料作物种植"),
//     new Array("a0169","茶及其他饮料作物种植"),
//     new Array("a0170","中药材种植"),
//     new Array("a0190","其他农业"),
//     new Array("a0211","林木育种"),
//     new Array("a0212","林木育苗"),
//     new Array("a0220","造林和更新"),
//     new Array("a0230","森林经营和管护"),
//     new Array("a0241","木材采运"),
//     new Array("a0242","竹材采运"),
//     new Array("a0251","木竹材林产品采集"),
//     new Array("a0252","非木竹材林产品采集"),
//     new Array("a0311","牛的饲养"),
//     new Array("a0312","马的饲养"),
//     new Array("a0313","猪的饲养"),
//     new Array("a0314","羊的饲养"),
//     new Array("a0315","骆驼饲养"),
//     new Array("a0319","其他牲畜饲养"),
//     new Array("a0321","鸡的饲养"),
//     new Array("a0322","鸭的饲养"),
//     new Array("a0323","鹅的饲养"),
//     new Array("a0329","其他家禽饲养"),
//     new Array("a0330","狩猎和捕捉动物"),
//     new Array("a0390","其他畜牧业"),
//     new Array("a0411","海水养殖"),
//     new Array("a0412","内陆养殖"),
//     new Array("a0421","海水捕捞"),
//     new Array("a0422","内陆捕捞"),
//     new Array("a0521","林业有害生物防治服务"),
//     new Array("a0522","森林防火服务"),
//     new Array("a0523","林产品初级加工服务"),
//     new Array("a0529","其他林业服务"),
//     new Array("a0540","渔业服务业"),
//     new Array("a0511","农业机械服务"),
//     new Array("a0512","灌溉服务"),
//     new Array("a0513","农产品初加工服务"),
//     new Array("a0519","其他农业服务"),
//     new Array("a0530","畜牧服务业"),
//     new Array("t9600","国际组织"),
//     new Array("b0610","烟煤和无烟煤开采洗选"),
//     new Array("b0620","褐煤开采洗选"),
//     new Array("b0690","其他煤炭采选"),
//     new Array("b0710","石油开采"),
//     new Array("b0720","天然气开采"),
//     new Array("b0820","锰矿、铬矿采选"),
//     new Array("b0890","其他黑色金属矿采选"),
//     new Array("b0810","铁矿采选"),
//     new Array("b0911","铜矿采选"),
//     new Array("b0912","铅锌矿采选"),
//     new Array("b0913","镍钴矿采选"),
//     new Array("b0914","锡矿采选"),
//     new Array("b0915","锑矿采选"),
//     new Array("b0916","铝矿采选"),
//     new Array("b0917","镁矿采选"),
//     new Array("b0919","其他常用有色金属矿采选"),
//     new Array("b0921","金矿采选"),
//     new Array("b0922","银矿采选"),
//     new Array("b0929","其他贵金属矿采选"),
//     new Array("b0931","钨钼矿采选"),
//     new Array("b0932","稀土金属矿采选"),
//     new Array("b0933","放射性金属矿采选"),
//     new Array("b0939","其他稀有金属矿采选"),
//     new Array("b1011","石灰石、石膏开采"),
//     new Array("b1012","建筑装饰用石开采"),
//     new Array("b1013","耐火土石开采"),
//     new Array("b1019","粘土及其他土砂石开采"),
//     new Array("b1020","化学矿开采"),
//     new Array("b1030","采盐"),
//     new Array("b1091","石棉、云母矿采选"),
//     new Array("b1092","石墨、滑石采选"),
//     new Array("b1093","宝石、玉石采选"),
//     new Array("b1099","其他未列明非金属矿采选"),
//     new Array("b1110","煤炭开采和洗选辅助活动"),
//     new Array("b1120","石油和天然气开采辅助活动"),
//     new Array("b1190","其他开采辅助活动"),
//     new Array("b1200","其他采矿业")
// );
//
//
// for(var i = 0; i<code4.length;i++){
//     var parent = code4[i][0].substr(0,4);
//     var parentNode = industryCode3[parent];
//     if(parentNode){
//         if(!parentNode.childrens){
//             parentNode.childrens = []
//         }
//         parentNode.childrens.push({ids:code4[i][0],name:code4[i][1]})
//     }
// }
//
// for(var key in industryCode3){
//     var parent = key.substr(0,3);
//     var parentNode = industryCode2[parent];
//     if(parentNode){
//         if(!parentNode.childrens){
//             parentNode.childrens = []
//         }
//         parentNode.childrens.push(industryCode3[key])
//     }
// }
//
// console.log(industryCode2);
//
// for(var key in industryCode2){
//     var parent = key.substr(0,1);
//     var parentNode = industry[parent];
//     if(parentNode){
//         if(!parentNode.childrens){
//             parentNode.childrens = []
//         }
//         parentNode.childrens.push(industryCode2[key])
//     }
// }
//
// console.log(industry);
//
// var industryArray = [];
// for(var key in industry){industryArray.push(industry[key]);}
//
// JSON.stringify(industryArray);