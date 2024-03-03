import dayjs from 'dayjs/esm';
import { IApsUser } from 'app/entities/aps-user/aps-user.model';
import { FieldAccess } from '../../shared/profile/view.models';

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

export interface ApsDeviceAccess {
  id: FieldAccess;
  username: FieldAccess;
  deviceId: FieldAccess;
  phoneModel: FieldAccess;
  osVersion: FieldAccess;
  installedOn: FieldAccess;
  apsUser: FieldAccess;
}

export let APS_DEVICE_ACCESS: ApsDeviceAccess;

APS_DEVICE_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  username: { listable: true, visible: true, disabled: true },
  deviceId: { listable: true, visible: true, disabled: true },
  phoneModel: { listable: true, visible: true, disabled: true },
  osVersion: { listable: true, visible: true, disabled: true },
  installedOn: { listable: true, visible: true, disabled: true },
  apsUser: { listable: false, visible: true, disabled: true },
}
