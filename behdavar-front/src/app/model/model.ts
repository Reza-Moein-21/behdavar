import {PhoneType} from "./enum/PhoneType";
import {ContractStatus} from "./enum/ContractStatus";
import {PaymentType} from "./enum/PaymentType";

class BaseModel<T> {
  id: T;
  version ?: number;
}


export class Guarantors extends BaseModel<number> {
  person: Person;
}

export class Person {
  name: string;
  family: string;
  fatherName?: string;
  birthDate?: Date;
  nationalNumber: string;
  postalCode?: string;
  mobile?: string;
  telephone?: string;
  birthPlace?: string;
  workPlacePhone?: string;
}

export class BaseAuditorDto<U, I> {
  id?: I;
  createdBy?: U;
  createdDate?: Date;
  lastModifiedBy?: U;
  lastModifiedDate?: Date;
  version?: number;
}

export class ContactDto extends BaseAuditorDto<string, number> {
  number?: string;
  description?: string;
  preCode?: string;
  confirmed?: boolean;
  phoneType?: PhoneType;
  person: PersonDto;
}


export class GuarantorDto extends BaseAuditorDto<string, number> {
  relationType: RelationType;
  contract: ContractDto;
  person: PersonDto;
}

export class CustomerDto extends BaseAuditorDto<string, number> {
  person: PersonDto;
  contract: ContractDto;
}

export class PersonDto extends BaseAuditorDto<string, number> {
  firstName: string;
  lastName: string;
  fullName: string;
  email: string;
  phoneNumber: string;
  description: string;
  contacts?: ContactDto[];
}

export class UserDto extends BaseAuditorDto<string, number> {
  firstName?: string;
  lastName?: string;
  fullName?: string = `${this.firstName} ${this.lastName}`
  username?: string;
  password?: string;
  code?: string;
  enabled?: boolean;
  tokenExpired?: boolean;
  isAccountNonExpired?: boolean;
  isAccountNonLocked?: boolean;
  isCredentialsNonExpired?: boolean;
  roles?: RoleDto[];
  person?: PersonDto;
}

export class PursuitDto extends BaseAuditorDto<string, number> {
  description: string;
  coordinateAppointment: boolean;
  // TODO use string for because use moment.format() to fix issue of Jackson convert to Java LocalDate. must fix...
  nextPursuitDate: string;
  customerDeposit: boolean;
  pursuitType: any;
  resultType: any;
  payment: PaymentDto;
  user: UserDto;
  contract: ContractDto;
}

export class PaymentDto extends BaseAuditorDto<string, number> {
  amount: number;
  paymentDate: Date;
  paymentType: PaymentType;
  contract: ContractDto;
  user: UserDto;
  attachment: AttachmentDto;
}

export class AttachmentDto extends BaseAuditorDto<string, number> {
  content: string;
  fileName: string;
  contract: ContractDto;
}

export class CatalogDto extends BaseAuditorDto<string, number> {
  id: number;
  code: string;
  englishTitle: string;
  title: string;
  active: boolean;
}

export class CartableDto extends BaseAuditorDto<string, number> {
  sender: UserDto;
  receiver: UserDto;
  contract: ContractDto;
}

export class ContractDto extends BaseAuditorDto<string, number> {
  contractNumber?: string;
  masterAmount?: number;
  defferedAmount?: number;
  idealIssueDate?: Date;
  lendingNumber?: string;
  defferedCount?: number;
  lateFees?: number;
  submitDate?: Date;
  lending?: LendingDto;
  product?: ProductDto;
  corporation?: CatalogDetailDto;
  contractStatus?: ContractStatus;
  contractColor?: ContractColor;
  contractType?: ContractType;
  customers?: CustomerDto[];
}

export class AssignContractDto {
  assigneeId: number;
  contractId: number;
  status: ContractStatus;
}

export class UserInfoDto extends BaseAuditorDto<string, number> {
  lastLogin: Date;
  userAmount: UserAmountDto;
  activeCount: number;
  user: UserDto;
}

export class UserAmountDto {
  totalAmount: number;
  receiveAmount: number;
  user: UserDto;
}

export class LendingDto extends BaseAuditorDto<string, number> {
  masterAmount: number;
  ideaIssueDate: Date;
  receiveLendingDate: Date;
  branchBank: BankDto;
  defferedAmount: number;
  lendingNumber: string;
  defferedCount: number;
  lateFees: number;
  remainDebtAmount: number;
}

export class ProductDto extends BaseAuditorDto<string, number> {
  productShasiNumber: string;
  productPlate: string;
  productName: string;
}

export class BankDto extends BaseAuditorDto<string, number> {
  code: string;
  name: string;
  phone: string;
  fax: string;
  address: AddressDto;
  heads: string;
  bankType: CatalogDetailDto;
  active: boolean;
}

export class CatalogDetailDto extends BaseAuditorDto<string, number> {
  englishTitle?: string;
  title?: string;
  code?: string;
  active?: boolean;
  catalog?: CatalogDto;

}

export class InputExcelDto extends BaseAuditorDto<string, number> {
  fileName: string;
  content: string;
}

export class AddressDto extends BaseAuditorDto<string, number> {
  mainStreet: string;
  subStreet: string;
  mainAlley: string;
  subAlley: string;
  postalCode: string;
  plate: string;
  description: string;
  geoDivision: GeoDivisionDto;
  person: Person;
}

export class GeoDivisionDto extends BaseAuditorDto<string, number> {
  code: string;
  name: string;
  geoDivisionType: GeoDivisionType;
  parent: GeoDivisionDto;
  children: GeoDivisionDto[];
}

export enum GeoDivisionType {
  PROVINCE = "PROVINCE",
  CITY = "CITY"
}

export class RoleDto extends BaseAuditorDto<string, number> {
  roleName: string;
  title: string;
  // TODO must user PrivilegeDto
  privileges: string[];
  privilegeDtos: PrivilegeDto[];
}

export class PrivilegeDto extends BaseAuditorDto<string, number> {
  name: string;
  title: string;
}


export enum ContractType {
  BANKS = " BANKS",
  CARS = " CARS"
}

export enum RelationType {
  GUARANTOR = "GUARANTOR",
  S1 = "S1",
  S2 = "S2"

}

export enum ContractColor {
  BLACK = "BLACK",
  RED = "RED",
  GREEN = "GREEN",
  BLUE = "BLUE",
  GRAY = "GRAY",
  PURPLE = 'PURPLE',
}


