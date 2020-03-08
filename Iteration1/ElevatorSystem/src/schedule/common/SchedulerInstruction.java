package schedule.common;

public class SchedulerInstruction {
	public enum INSTRUCTION {
		ELEVETOR_STOP{
			public byte getValue() {
				return 0;
			}
		},
		ELEVETOR_UP{
			public byte getValue() {
				return 1;
			}
		},
		ELEVETOR_DOWN{
			public byte getValue() {
				return 2;
			}
		},
		ELEVETOR_WAIT{
			public byte getValue() {
				return 3;
			}
		};

		public abstract byte getValue();
	}
}
