import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { InvoiceItemService } from '../service/invoice-item.service';
import { IInvoiceItem } from '../invoice-item.model';
import { InvoiceItemFormService } from './invoice-item-form.service';

import { InvoiceItemUpdateComponent } from './invoice-item-update.component';

describe('InvoiceItem Management Update Component', () => {
  let comp: InvoiceItemUpdateComponent;
  let fixture: ComponentFixture<InvoiceItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceItemFormService: InvoiceItemFormService;
  let invoiceItemService: InvoiceItemService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InvoiceItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvoiceItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceItemFormService = TestBed.inject(InvoiceItemFormService);
    invoiceItemService = TestBed.inject(InvoiceItemService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Invoice query and add missing value', () => {
      const invoiceItem: IInvoiceItem = { id: 456 };
      const invoice: IInvoice = { id: 22319 };
      invoiceItem.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 30219 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(expect.objectContaining),
      );
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceItem: IInvoiceItem = { id: 456 };
      const invoice: IInvoice = { id: 14856 };
      invoiceItem.invoice = invoice;

      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection).toContain(invoice);
      expect(comp.invoiceItem).toEqual(invoiceItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItem>>();
      const invoiceItem = { id: 123 };
      jest.spyOn(invoiceItemFormService, 'getInvoiceItem').mockReturnValue(invoiceItem);
      jest.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceItemFormService.getInvoiceItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceItemService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItem>>();
      const invoiceItem = { id: 123 };
      jest.spyOn(invoiceItemFormService, 'getInvoiceItem').mockReturnValue({ id: null });
      jest.spyOn(invoiceItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceItem }));
      saveSubject.complete();

      // THEN
      expect(invoiceItemFormService.getInvoiceItem).toHaveBeenCalled();
      expect(invoiceItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceItem>>();
      const invoiceItem = { id: 123 };
      jest.spyOn(invoiceItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoice', () => {
      it('Should forward to invoiceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
