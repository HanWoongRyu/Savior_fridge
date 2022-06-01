import pickle
import torch
import torchvision.transforms as transforms
import initialize
from datasets import LotteProductDataset
from timm.data import resolve_data_config, create_loader
from multiprocessing import freeze_support

class LotteClassification:
    def __init__(self):
        self.cfg = None
        self.device = None
        self.model = None
        self.label_to_name = None
        self.label_dict = None

    def _init_from_cfg(self, cfg, device):
        self.device = device

        self.model = initialize.init_model(cfg=cfg, device=device)
        self.optimizer = initialize.init_optimizer(cfg=cfg, model=self.model)
        self.scheduler, self.num_epochs = initialize.init_scheduler(cfg=cfg, optimizer=self.optimizer)

        self.data_config = resolve_data_config(dict(cfg.dataset), model=self.model)
        self.pre_transform = transforms.Resize((256, 256)) if cfg.dataset.pre_transform else None

    def load_checkpoint(self, checkpoint_path, device='cuda'):
        checkpoint = torch.load(checkpoint_path)

        self.cfg = checkpoint['cfg']
        self._init_from_cfg(self.cfg, device)

        self.model.module.load_state_dict(checkpoint['model'])
        self.label_dict = checkpoint['label_to_name']

    def inference(self, data_root):
        test_set = LotteProductDataset(root=data_root, partition='test', train_label=self.label_to_name,
                                    pre_transform=self.pre_transform, with_idx=True)
        test_loader = create_loader(test_set,
                                    input_size=self.data_config['input_size'],
                                    batch_size=self.cfg.dataset.batch_size,
                                    is_training=False,
                                    interpolation=self.data_config['interpolation'],
                                    mean=self.data_config['mean'],
                                    std=self.data_config['std'],
                                    num_workers=12)

        for a in test_loader:
            input1 = a[0].to(self.device)
            self.model.eval()
            output = self.model(input1)
            pred_label = output.max(1)[1]
            pred_name = []
            for label in pred_label.tolist():
                pred_name.append(self.label_dict[label])

        return pred_name

if __name__ == '__main__':
    freeze_support()
    l1 = LotteClassification()
    l1.load_checkpoint('best_top1_validation.pth')
    
    with open('food_name_dict.pkl', 'rb') as f:
        mydict = pickle.load(f)

    for pred in l1.inference('.\\data'):
        print(mydict[pred])
