import dayjs from 'dayjs/esm';
import { IApsUser } from 'app/entities/aps-user/aps-user.model';

export interface IApsDevice {
  id: number;
  username?: string | null;
  deviceId?: string | null;
  phoneModel?: string | null;
  osVersion?: string | null;
  installedOn?: dayjs.Dayjs | null;
  apsUser?: IApsUser | null;
}

export type NewApsDevice = Omit<IApsDevice, 'id'> & { id: null };
