import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;

import org.springframework.util.CollectionUtils;

public class TestSplitOrder {
	public static List<OrderTest> orderList;
	public static List<EmpTest> empList;
	public static HashMap<Integer, List<OrderTest>> empOrder;

	static {
		orderList = new ArrayList<>();
		empList = new ArrayList<>();
		empOrder = new HashMap<>();
		for (int i = 0; i < 2000; i++) {
			orderList.add(OrderTest.newBuilder().orderNo("Order_" + i)
					.orderLevel(getRandomChar()).orderLevelSort(getRandom(10))
					.build());
		}
		for (int i = 0; i < 10; i++) {
			empList.add(EmpTest.newBuilder().empLevel(getRandom(5))
					.empNo("Emp_" + i).id(i).mapingOrderLevel(getRandomChar())
					.orderNumLimit(getRandom(20)).build());
		}
	}

	public static String getRandomChar() {
		String chars = "ABCD";
		return String.valueOf(chars.charAt((int) (Math.random() * 4)));
	}

	public static Integer getRandom(int a) {

		Random random = new Random();
		return random.nextInt(a);
	}

	@SuppressWarnings("unchecked")
	public static void SpiltOrder(
			ThreeTuple<List<OrderTest>, List<EmpTest>, HashMap<Integer, List<OrderTest>>> splitInfo) {
		Long start = new Date().getTime();
		// ���ֶ���
		List<OrderTest> orderList = splitInfo.getFirst();
		// Ա������
		List<EmpTest> empList = splitInfo.getSecond();
		// Ա������ӳ�伯�ϣ�key��Ա����empNo,value�Ƿָ����ĵ���
		HashMap<Integer, List<OrderTest>> empOrderMap = splitInfo.getThird();
		// �ж��Ƿ��ж��������ķֵ������
		if (CollectionUtils.isEmpty(orderList)
				|| checkEmpLimit(empList, empOrderMap)) {
			// �ֵ�����
			return;
		}
		// ������Ա���ֵ�˳������:Ա������ӵ͵�������
		Collections.sort(empList, new Comparator() {
			public int compare(Object a, Object b) {
				int one = ((EmpTest) a).getEmpLevel();
				int two = ((EmpTest) b).getEmpLevel();
				return one - two;
			}
		});
		
		
		//��������orderLevelSort�ӵ͵��߽�������
		Collections.sort(orderList, new Comparator() {
			public int compare(Object a, Object b) {
				int one = ((OrderTest) a).getOrderLevelSort();
				int two = ((OrderTest) b).getOrderLevelSort();
				return one - two;
			}
		});
		

		// �ֵ�
		EmpTest currentEmp = null;
		for (OrderTest order : orderList) {
			// Ա���б� ��������
			if (null != currentEmp) {
				int currentEmpIndex = empList.indexOf(currentEmp);
				List<EmpTest> emplist2 = empList.subList(currentEmpIndex + 1,
						empList.size());
				emplist2.addAll(empList.subList(0, currentEmpIndex + 1));
				empList = emplist2;
			}

			for (EmpTest emp : empList) {
				// ��ǰԱ���ļ������˵�ǰ����
				if (!emp.getMapingOrderLevel().contains(order.getOrderLevel())) {
					continue;
				}
				if (emp.getOrderNumLimit().intValue() == 0) {
					continue;
				}
				// ��⵱ǰ�ֵ�����
				if (checkEmpLimit(empList, empOrderMap)) {
					return;
				}
				if (!CollectionUtils.isEmpty(empOrderMap.get(emp.getId()))) {
					if (empOrderMap.get(emp.getId()).size() == emp
							.getOrderNumLimit()) {
						continue;
					}
					empOrderMap.get(emp.getId()).add(order);
					currentEmp = emp;
					break;
				}

				// ��empOrderMapΪ�յ�ʱ�򣬵�һ�������и�ֵ
				List<OrderTest> tempList = new ArrayList<>();
				tempList.add(order);
				empOrderMap.put(emp.getId(), tempList);
				currentEmp = emp;
				break;
			}
		}
		Long end = new Date().getTime();
		System.out.println("�ֵ�ʱ�䣺" + (end - start));

	}

	public static Boolean checkEmpLimit(List<EmpTest> empList,
			HashMap<Integer, List<OrderTest>> empOrderMap) {
		// ѭ�����е�
		for (EmpTest emp : empList) {

			if (null == empOrderMap.get(emp.getId())) {
				return false;
			}
			if (emp.getOrderNumLimit().intValue() != empOrderMap.get(
					emp.getId()).size()) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		//System.out.println("A|B".contains("A"));
		System.out.println("����list:" + JSON.toJSON(orderList));
		System.out.println("Ա��list:" + JSON.toJSON(empList));

		HashMap<Integer, List<OrderTest>> tempMap = new HashMap<>();
		SpiltOrder(new ThreeTuple(orderList, empList, tempMap));
		System.out.println(JSON.toJSON(tempMap));
	}

}
