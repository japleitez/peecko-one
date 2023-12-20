import dayjs from 'dayjs/esm';

import { IApsDevice, NewApsDevice } from './aps-device.model';

export const sampleWithRequiredData: IApsDevice = {
  id: 313,
  username: 'though official',
  deviceId: 'since breakfast heavily',
};

export const sampleWithPartialData: IApsDevice = {
  id: 30313,
  username: 'readily certainly cruelly',
  deviceId: 'boohoo um',
  phoneModel: 'judicious movie hmph',
  osVersion: 'hm bread',
};

export const sampleWithFullData: IApsDevice = {
  id: 30781,
  username: 'whoa psychologist following',
  deviceId: 'terrific',
  phoneModel: 'wimp zowie',
  osVersion: 'kissingly',
  installedOn: dayjs('2023-12-20T05:46'),
};

export const sampleWithNewData: NewApsDevice = {
  username: 'league',
  deviceId: 'sorrowful aside',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
